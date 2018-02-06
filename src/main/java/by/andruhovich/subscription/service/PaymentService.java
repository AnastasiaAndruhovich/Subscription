package by.andruhovich.subscription.service;

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
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PaymentService extends BaseService {
    public Payment addPayment(String userId, String subscriptionId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        int intSubscriptionId = Integer.parseInt(subscriptionId);
        int intUserId = Integer.parseInt(userId);
        Date date = Calendar.getInstance().getTime();

        try {
            connection = connectionFactory.getConnection();
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO(connection);
            UserDAO userDAO = new UserDAO(connection);
            PaymentDAO paymentDAO = new PaymentDAO(connection);
            AccountDAO accountDAO = new AccountDAO(connection);
            Subscription subscription = subscriptionDAO.findEntityById(intSubscriptionId);
            List<Subscription> subscriptions = new LinkedList<>();
            subscriptions.add(subscription);
            subscription = FillOutEntityService.fillOutSubscriptionList(subscriptions).get(0);
            Account account = userDAO.findAccountByUserId(intUserId);
            boolean statement = account.getBalance().compareTo(subscription.getPublication().getPrice()) > 0;
            Payment payment = new Payment(subscription.getPublication().getPrice(), date, statement, subscription);
            int paymentId = paymentDAO.create(payment);
            if (paymentId != -1 && statement) {
                accountDAO.withdraw(account.getAccountNumber(), subscription.getPublication().getPrice());
                subscription.setSubscriptionIsActive(true);
                subscriptionDAO.update(subscription);
                payment.setPaymentNumber(paymentId);
            }
            return payment;
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }
}
