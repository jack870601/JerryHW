
/*****************************************************************
    CS4001301 Programming Languages                   
    
    Programming Assignment #1
    
    Java programming using subtype, subclass, and exception handling
    
    To compile: %> javac Application.java
    
    To execute: %> java Application

******************************************************************/

import java.util.*;
import java.text.SimpleDateFormat;
class BankingException extends Exception {
    BankingException () { super(); }
    BankingException (String s) { super(s); }
} 

interface BasicAccount {
    String name();
    double balance();	
}

interface WithdrawableAccount extends BasicAccount {
    double withdraw(double amount) throws BankingException;	
}

interface DepositableAccount extends BasicAccount {
    double deposit(double amount) throws BankingException;	
}

interface InterestableAccount extends BasicAccount {
    double computeInterest() throws BankingException;	
}

interface FullFunctionalAccount extends WithdrawableAccount,
                                        DepositableAccount,
                                        InterestableAccount {
}

public abstract class Account {
	
    // protected variables to store commom attributes for every bank accounts	
    protected String accountName;
    protected double accountBalance;
    protected double accountInterestRate;
    protected Date openDate;
    protected Date lastInterestDate;
    
    // public methods for every bank accounts
    public String name() {
    	return(accountName);	
    }	
    
    public double balance() {
        return(accountBalance);
    }


    abstract double deposit(double amount, Date depositDate) throws BankingException;
    
    public double deposit(double amount) throws BankingException{
        //accountBalance += amount;
        Date depositDate = new Date();
        return(deposit(amount,depositDate));
    } 
    
    abstract double withdraw(double amount, Date withdrawDate) throws BankingException;
    
    public double withdraw(double amount) throws BankingException {
        Date withdrawDate = new Date();
        return(withdraw(amount, withdrawDate));
    }
    
    abstract double computeInterest(Date interestDate) throws BankingException;
    
    public double computeInterest() throws BankingException {
        Date interestDate = new Date();
        return(computeInterest(interestDate));
    }
}

/*
 *  Derived class: CheckingAccount
 *
 *  Description:
 *      Interest is computed daily; there's no fee for
 *      withdraw; there is a minimum balance of $1000.
 */
                          
class CheckingAccount extends Account implements FullFunctionalAccount {

    CheckingAccount(String s, double firstDeposit,Date firstDate,double interestRate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = interestRate;
        openDate = firstDate;
        lastInterestDate = openDate;	
    }
    
    // CheckingAccount(String s, double firstDeposit, Date firstDate) {
    //     accountName = s;
    //     accountBalance = firstDeposit;
    //     accountInterestRate = 0.12;
    //     openDate = firstDate;
    //     lastInterestDate = openDate;	
    // }	
    
    public double deposit(double amount, Date depositDate) throws BankingException {
            accountBalance += amount;   
            return(accountBalance);     
                                                  
    }

    public double withdraw(double amount, Date withdrawDate) throws BankingException {
    // minimum balance is 1000, raise exception if violated
        if ((accountBalance  - amount) < 1000) {
            throw new BankingException ("Underfraft from checking account name:" +
                                         accountName);
        } else {
            accountBalance -= amount;	
            return(accountBalance); 	
        }                                        	
    }
    
    public double computeInterest (Date interestDate) throws BankingException {
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException ("Invalid date to compute interest for account name : < " +
                                        accountName +" >");                            	
        }
        
        int numberOfDays = (int) ((interestDate.getTime() 
                                   - lastInterestDate.getTime())
                                   / 86400000.0);
        System.out.println("Number of days since last interest is " + numberOfDays);
        double interestEarned = (double) numberOfDays / 365.0 *
                                      accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned); 
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return(accountBalance);                            
    }  	
}           

class SavingAccount extends Account implements FullFunctionalAccount {
    protected String[] transactionMonth;
    protected int lastMonth;
    protected int lastYear;
    protected int fee;
    protected int freeFeeCount;
    SavingAccount(String s, double firstDeposit,Date firstDate,double interestRate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = interestRate;
        openDate = firstDate;
        lastInterestDate = openDate;
        SimpleDateFormat year =  new SimpleDateFormat("yyyy");
        SimpleDateFormat month =  new SimpleDateFormat("MM");
        lastYear=Integer.valueOf(year.format(openDate));
        lastMonth=Integer.valueOf(month.format(openDate));
        fee=0; 
        freeFeeCount=3;
        //transactionMonth = new String[1];    
    }
    


    public double deposit(double amount, Date depositDate) throws BankingException {

        int nowMonth;
        int nowYear;
        SimpleDateFormat year =  new SimpleDateFormat("yyyy");
        SimpleDateFormat month =  new SimpleDateFormat("MM");
        nowYear=Integer.valueOf(year.format(depositDate));
        nowMonth=Integer.valueOf(month.format(depositDate));

        if((nowYear==lastYear) && (nowMonth==lastMonth)){
            if(freeFeeCount!=0){
            freeFeeCount--;
            }else{
            fee=1;
            }
            
        }else if ((nowYear==lastYear) && (nowMonth!=lastMonth)){
            lastMonth=nowMonth;
            freeFeeCount=2;
            fee =0;
        }

        System.out.println ("The fee is : "+ fee);
            if ((accountBalance + amount - fee) < 0) {
                throw new BankingException ("Sorry,your balance is not enough to pay the handling fee  : <" +
                                         accountName+" >");
            }
            else{
                 accountBalance += amount; 
                 accountBalance -= fee;
            }
           
            return(accountBalance);     
                                                  
    }

    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        
        //每個月的前三次免費 fee=0  還沒改
        ////////////////////////////////
        int nowMonth;
        int nowYear;
        SimpleDateFormat year =  new SimpleDateFormat("yyyy");
        SimpleDateFormat month =  new SimpleDateFormat("MM");
        nowYear=Integer.valueOf(year.format(withdrawDate));
        nowMonth=Integer.valueOf(month.format(withdrawDate));

       

        if((nowYear==lastYear) && (nowMonth==lastMonth)){
            if(freeFeeCount!=0){
            freeFeeCount--;
            }else{
            fee=1;
            }
            
        }else if ((nowYear==lastYear) && (nowMonth!=lastMonth)){
            lastMonth=nowMonth;
            freeFeeCount=2;
            fee =0;
        }
        ////////////////////////////////
        


        
        System.out.println ("The fee is : "+ fee);
        if ((accountBalance  - amount - fee) < 0) {
            throw new BankingException ("Sorry,your balance is not enough to withdraw : <" +
                                         accountName+" >");
        } else {
            accountBalance -= amount;
            accountBalance -= fee;
            
            return(accountBalance);     
        }                                           





    }
    public double computeInterest (Date interestDate) throws BankingException {
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException ("Invalid date to compute interest for account name : <" +
                                        accountName+" >");                               
        }
 
        SimpleDateFormat year =  new SimpleDateFormat("yyyy");
        SimpleDateFormat month =  new SimpleDateFormat("MM");
        int numberOfMonth = (Integer.valueOf(year.format(interestDate))-Integer.valueOf(year.format(lastInterestDate))) * 12;
        //(交易的年-最早交易的年)*12 代表經過幾年。然後再換算成幾個月
        numberOfMonth+=(Integer.valueOf(month.format(interestDate))-Integer.valueOf(month.format(lastInterestDate)));
        //再把交易的月數-最早交易的月 代表經過幾個月 再把他加上去




        System.out.println("Number of months since last interest is " + numberOfMonth );

        double interestEarned = (double) (numberOfMonth * accountInterestRate * accountBalance / 12 );
        System.out.println("Interest earned is " + interestEarned); 
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return(accountBalance);                            
    }
}
class CDAccount extends Account implements FullFunctionalAccount {
    protected int cdMonDurationCount;
     CDAccount(String s, double firstDeposit ,Date firstDate,double interestRate,int cdMonDuration) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = interestRate;
        openDate = firstDate;
        lastInterestDate = openDate;
        cdMonDurationCount=cdMonDuration;
    }

    public double deposit(double amount, Date depositDate) throws BankingException {

        SimpleDateFormat year =  new SimpleDateFormat("yyyy");
        SimpleDateFormat month =  new SimpleDateFormat("MM");
        int numberOfMonth = (Integer.valueOf(year.format(depositDate))-Integer.valueOf(year.format(openDate))) * 12;
        //(交易的年-最早交易的年)*12 代表經過幾年。然後再換算成幾個月

        numberOfMonth+=(Integer.valueOf(month.format(depositDate))-Integer.valueOf(month.format(openDate)));
        //再把交易的月數-最早交易的月 代表經過幾個月 再把他加上去

            if(numberOfMonth<cdMonDurationCount){
                throw new BankingException ( "This duration has not ended yet: <" +  accountName + " >You still have to wait:"+(cdMonDurationCount-numberOfMonth)+" months");
            }
            else{
            System.out.println("The duration has ended. You can deposit !");
            accountBalance += amount;   
            return(accountBalance);     
            }
                                                  
    }

    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        int withdrawFee = 250;


        SimpleDateFormat year =  new SimpleDateFormat("yyyy");
        SimpleDateFormat month =  new SimpleDateFormat("MM");
        int numberOfMonth = (Integer.valueOf(year.format(withdrawDate))-Integer.valueOf(year.format(openDate))) * 12;
        //(交易的年-最早交易的年)*12 代表經過幾年。然後再換算成幾個月
        numberOfMonth+=(Integer.valueOf(month.format(withdrawDate))-Integer.valueOf(month.format(openDate)));
        //再把交易的月數-最早交易的月 代表經過幾個月 再把他加上去


        if(numberOfMonth<cdMonDurationCount){
            withdrawFee=250;
        }else{
            withdrawFee=0;
        }




        if ((accountBalance-amount-withdrawFee)<0){
            throw new BankingException ("Invalid date to compute interest for account name : < " +accountName+" >");         
        }else{
            System.out.println("the fee is : $"+withdrawFee);
            accountBalance=(accountBalance-amount-withdrawFee);
            return(accountBalance);
        }
    }
    public double computeInterest (Date interestDate) throws BankingException {

        if (interestDate.before(lastInterestDate)) {
            throw new BankingException ("Invalid date to compute interest for account name : < " +
                                        accountName+" >");                               
        }
 
        SimpleDateFormat year =  new SimpleDateFormat("yyyy");
        SimpleDateFormat month =  new SimpleDateFormat("MM");
        int numberOfMonth = (Integer.valueOf(year.format(interestDate))-Integer.valueOf(year.format(lastInterestDate))) * 12;
        //(交易的年-最早交易的年)*12 代表經過幾年。然後再換算成幾個月
        numberOfMonth+=(Integer.valueOf(month.format(interestDate))-Integer.valueOf(month.format(lastInterestDate)));
        //再把交易的月數-最早交易的月 代表經過幾個月 再把他加上去



        System.out.println("Number of months since last interest is " + numberOfMonth );


        double interestEarned = (double) (numberOfMonth * accountInterestRate * accountBalance / 12);
        System.out.println("Interest earned is " + interestEarned); 
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return(accountBalance);                            

    }
}



class LoanAccount extends Account implements FullFunctionalAccount {

    LoanAccount(String s, double firstDeposit,Date firstDate,double interestRate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = interestRate;
        openDate = firstDate;
        lastInterestDate = openDate;   
    }


    public double deposit(double amount, Date depositDate) throws BankingException {

            if(amount>Math.abs(accountBalance)){
                throw new BankingException ("Invalid amount to deposit money into Account: <" + accountName+" >");     
            }
            else{
                accountBalance += amount;   
                return(accountBalance);     
            }
            
    }

    //不能提款 欠錢了還想提款？
    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        throw new BankingException ("Sorry,you can't withdraw because you owe the bank money：<" + accountName  +" >");
    }




    public double computeInterest (Date interestDate) throws BankingException {
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException ("Invalid date to compute interest for account name : <" +
                                        accountName+" >");                               
        }
 
        SimpleDateFormat year =  new SimpleDateFormat("yyyy");
        SimpleDateFormat month =  new SimpleDateFormat("MM");
        int numberOfMonth = (Integer.valueOf(year.format(interestDate))-Integer.valueOf(year.format(lastInterestDate))) * 12;
        //(交易的年-最早交易的年)*12 代表經過幾年。然後再換算成幾個月
        numberOfMonth+=(Integer.valueOf(month.format(interestDate))-Integer.valueOf(month.format(lastInterestDate)));
        //再把交易的月數-最早交易的月 代表經過幾個月 再把他加上去



        System.out.println("Number of months since last interest is " + numberOfMonth );

        double interestEarned = (double) (numberOfMonth* accountInterestRate * accountBalance / 12 );
        System.out.println("Interest earned is " + interestEarned); 

        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        
        return(accountBalance);                       
    }
}


