package com.mystore.testcases;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.mystore.pageobject.accountCreationDetails;
import com.mystore.pageobject.indexPage;
import com.mystore.pageobject.myAccount;
import com.mystore.pageobject.registeredUserAccount;
import com.mystore.utilities.ReadExcelFile;

public class TC_MyAccountPageDataDrivenTesting extends BaseClass {
	
	@Test(enabled=false)
	public void verifyRegistrationAndLogin() {
		// LaunchBrowser
		
		indexPage pg =new indexPage(driver);
		pg.clickOnSignIn();
		logger.info("Clicked on SignIn Button");
		
		myAccount myAccpg = new myAccount(driver);
		myAccpg.enterCreateEmailAddress("radhadabir@gmail.com");
		logger.info("entered email");
		myAccpg.clickSubmit();
		logger.info("Clicked on submit  Button");
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		accountCreationDetails accCreationPg = new accountCreationDetails(driver);
		
		accCreationPg.selectTitleMr();
		logger.info("title mr is selected");
		accCreationPg.enterCustomerFirstName("radha");
		logger.info("entered first name");
		accCreationPg.enterCustomersLastName("dabir");
		logger.info("entered last name");
		accCreationPg.enterPassword("radhadabir");
		logger.info("entered password");
		accCreationPg.Register();
		logger.info("clicked on register button");
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		registeredUserAccount registeredUserAcc = new registeredUserAccount(driver);
		String username = registeredUserAcc.getUserName();
		
		Assert.assertEquals("Radha dabir", username); 
		logger.info("verified username");
		
		
		
	}
	@Test(dataProvider = "LoginDataProvider")
	public void VerifyLogin(String userEmail, String userPwd, String expectedUsername) throws IOException 
	{

		logger.info("***************TestCase VerifyLogin starts*****************"); 


		indexPage pg = new indexPage(driver);

		pg.clickOnSignIn();
		logger.info("Clicked on sign in link");

		myAccount myAcpg = new myAccount(driver);

		myAcpg.enterLoginEmailAddress(userEmail);
		logger.info("Entered email address");
		
		myAcpg.enterLoginPassword(userPwd);
		logger.info("Entered password");

		myAcpg.clickLogin();
		logger.info("Clicked on sign in link..");

		registeredUserAccount regUser = new registeredUserAccount(driver);
		String userName = regUser.getUserName();
		
		
		if(userName.equals(expectedUsername))
		{
			logger.info("VerifyLogin - Passed");
			Assert.assertTrue(true);
			
			regUser.signout();
			
		}
		else
		{
			logger.info("VerifyLogin - Failed");
			captureScreenshots(driver,"VerifyLogin");
			Assert.assertTrue(false);

		} 		 
		logger.info("***************TestCase Verify login ends*****************"); 
	}

	@DataProvider(name = "LoginDataProvider")
	public String[][] LoginDataProvider()
	{
		//System.out.println(System.getProperty("user.dir"));
		String fileName = System.getProperty("user.dir") + "\\TestData\\MystoreTestData.xlsx";


		int ttlRows = ReadExcelFile.getRowCount(fileName, "LoginTestData");
		int ttlColumns = ReadExcelFile.getColCount(fileName, "LoginTestData");
	

		String data[][]=new String[ttlRows-1][ttlColumns];

		for(int i=1;i<ttlRows;i++)//rows =1,2
		{
			for(int j=0;j<ttlColumns;j++)//col=0, 1,2
			{

				data[i-1][j]=ReadExcelFile.getCellValue(fileName,"LoginTestData", i,j);
			}

		}
		return data;
	}
	
	

}
