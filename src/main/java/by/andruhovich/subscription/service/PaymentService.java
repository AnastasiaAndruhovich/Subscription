package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.AccountManagerDAO;
import by.andruhovich.subscription.dao.PaymentManagerDAO;
import by.andruhovich.subscription.dao.SubscriptionManagerDAO;
import by.andruhovich.subscription.dao.UserManagerDAO;
import by.andruhovich.subscription.dao.impl.AccountDAO;
import by.andruhovich.subscription.dao.impl.PaymentDAO;
import by.andruhovich.subscription.dao.impl.SubscriptionDAO;
import by.andruhovich.subscription.dao.impl.UserDAO;
import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.entity.Payment;
import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.pool.ConnectionFactory;

import java.sql.Connection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides methods to process payment information
 */
public class PaymentService extends BaseService {
    /**
     * @param userId User id
     * @param subscriptionId Subscription id
     * @return Created Payment
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public Payment addPayment(String userId, String subscriptionId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        int intSubscriptionId = Integer.parseInt(subscriptionId);
        int intUserId = Integer.parseInt(userId);
        Date date = new Date();

        try {
            connection = connectionFactory.getConnection();
            SubscriptionManagerDAO subscriptionManagerDAO = new SubscriptionDAO(connection);
            UserManagerDAO userManagerDAO = new UserDAO(connection);
            PaymentManagerDAO paymentManagerDAO = new PaymentDAO(connection);
            AccountManagerDAO accountManagerDAO = new AccountDAO(connection);
            Subscription subscription = subscriptionManagerDAO.findEntityById(intSubscriptionId);
            List<Subscription> subscriptions = new LinkedList<>();
            subscriptions.add(subscription);
            subscription = FillOutEntityService.fillOutSubscriptionList(subscriptions).get(0);
            Account account = userManagerDAO.findAccountByUserId(intUserId);
            boolean statement = account.getBalance().compareTo(subscription.getPublication().getPrice()) > 0;
            Payment payment = new Payment(subscription.getPublication().getPrice(), date, statement, subscription);
            int paymentId = paymentManagerDAO.create(payment);
            if (paymentId != -1 && statement) {
                accountManagerDAO.withdraw(account.getAccountNumber(), subscription.getPublication().getPrice());
                subscription.setSubscriptionIsActive(true);
                subscriptionManagerDAO.update(subscription);
                payment.setPaymentNumber(paymentId);
            }
            return payment;
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param userId User id in database
     * @param pageNumber Current page number in database
     * @return Payment list relevant to user in database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public List<Payment> findPaymentByUserId(String userId, String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intUserId = Integer.parseInt(userId);

        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;

        try {
            connection = connectionFactory.getConnection();
            PaymentManagerDAO paymentManagerDAO = new PaymentDAO(connection);
            List<Payment> payments = paymentManagerDAO.findPaymentsByUserId(intUserId, startIndex, endIndex);
            return FillOutEntityService.fillOutPaymentList(payments);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }

    }

    /**
     * @return Payment count in database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public int findPaymentPageCount() throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            PaymentDAO paymentDAO = new PaymentDAO(connection);
            int count = paymentDAO.findEntityCount();
            return (int)Math.ceil((double)(count) / ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }
}
