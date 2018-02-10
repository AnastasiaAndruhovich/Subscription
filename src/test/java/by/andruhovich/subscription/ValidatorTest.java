package by.andruhovich.subscription;

import by.andruhovich.subscription.validator.ServiceValidator;
import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

public class ValidatorTest {
    private String correctVariable;
    private String incorrectLengthVariable;
    private String incorrectSymbolVariable;
    private String negativeVariable = null;

    @BeforeGroups("PostalIndex")
    public void postalIndexInit() {
        correctVariable = "246028";
        incorrectLengthVariable = "1231237";
        incorrectSymbolVariable = "fgf546";
    }

    @BeforeGroups("Login")
    public void loginInit() {
        correctVariable = "nastya_andr";
        incorrectLengthVariable = "rt54";
        incorrectSymbolVariable = "qwe!bjh#";
        incorrectSymbolVariable = "bytu_*hjgt";
    }

    @BeforeGroups("Password")
    public void passwordInit() {
        correctVariable = "1267bvcd3";
        incorrectLengthVariable = "3v5";
        incorrectSymbolVariable = "@3hjg$td";
    }

    @BeforeGroups("Price")
    public void priceInit() {
        correctVariable = "12.09";
        incorrectLengthVariable = "56.098";
        incorrectSymbolVariable = "ggt76,99";
    }

    @BeforeGroups("Date")
    public void dateInit() {
        correctVariable = "1978-12-08";
        incorrectLengthVariable = "123-89-65";
        incorrectSymbolVariable = "456-ghgvf5t-12";
    }

    @BeforeGroups("Name")
    public void nameInit() {
        correctVariable = "Асадов";
        incorrectLengthVariable = "jFgfgfhjgtyughgvbhnmмтимппранеаепитпмкhghfhg";
        incorrectSymbolVariable = "Эдуард78";
    }

    @Test(groups = "PostalIndex")
    public void postalIndexPositiveTest() {
        Assert.assertTrue(ServiceValidator.verifyPostalIndex(correctVariable));
    }

    @Test(groups = "PostalIndex")
    public void postalIndexLengthNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyPostalIndex(incorrectLengthVariable));
    }

    @Test(groups = "PostalIndex")
    public void postalIndexSymbolNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyPostalIndex(incorrectSymbolVariable));
    }

    @Test(groups = "PostalIndex")
    public void postalIndexNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyPostalIndex(negativeVariable));
    }

    @Test(groups = "Login")
    public void loginPositiveTest() {
        Assert.assertTrue(ServiceValidator.verifyLogin(correctVariable));
    }

    @Test(groups = "Login")
    public void loginLengthNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyLogin(incorrectLengthVariable));
    }

    @Test(groups = "Login")
    public void loginSymbolNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyLogin(incorrectSymbolVariable));
    }

    @Test(groups = "Login")
    public void loginNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyLogin(negativeVariable));
    }

    @Test(groups = "Password")
    public void passwordPositiveTest() {
        Assert.assertTrue(ServiceValidator.verifyPassword(correctVariable));
    }

    @Test(groups = "Password")
    public void passwordLengthNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyPassword(incorrectLengthVariable));
    }

    @Test(groups = "Password")
    public void passwordSymbolNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyPassword(incorrectSymbolVariable));
    }

    @Test(groups = "Password")
    public void passwordNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyPassword(negativeVariable));
    }

    @Test(groups = "Price")
    public void pricePositiveTest() {
        Assert.assertTrue(ServiceValidator.verifyPrice(correctVariable));
    }

    @Test(groups = "Price")
    public void priceLengthNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyPrice(incorrectLengthVariable));
    }

    @Test(groups = "Price")
    public void priceSymbolNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyPrice(incorrectSymbolVariable));
    }

    @Test(groups = "Price")
    public void priceNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyPrice(negativeVariable));
    }

    @Test(groups = "Date")
    public void datePositiveTest() {
        Assert.assertTrue(ServiceValidator.verifyDate(correctVariable));
    }

    @Test(groups = "Date")
    public void dateLengthNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyDate(incorrectLengthVariable));
    }

    @Test(groups = "Date")
    public void dateSymbolNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyDate(incorrectSymbolVariable));
    }

    @Test(groups = "Date")
    public void dateNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyDate(negativeVariable));
    }

    @Test(groups = "Name")
    public void namePositiveTest() {
        Assert.assertTrue(ServiceValidator.verifyName(correctVariable));
    }

    @Test(groups = "Name")
    public void nameLengthNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyName(incorrectLengthVariable));
    }

    @Test(groups = "Name")
    public void nameSymbolNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyName(incorrectSymbolVariable));
    }

    @Test(groups = "Name")
    public void nameNegativeTest() {
        Assert.assertFalse(ServiceValidator.verifyName(negativeVariable));
    }
}
