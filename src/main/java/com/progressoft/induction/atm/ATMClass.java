package com.progressoft.induction.atm;

import com.progressoft.induction.atm.exceptions.AccountNotFoundException;
import com.progressoft.induction.atm.exceptions.InsufficientFundsException;
import com.progressoft.induction.atm.exceptions.NotEnoughMoneyInATMException;

import java.math.BigDecimal;
import java.util.*;

public class ATMClass implements ATM ,BankingSystem{

            HashMap<String , BigDecimal> accountDetails = new HashMap<>();
            HashMap<Banknote , BigDecimal> bankNotes = new HashMap<>();

    public ATMClass(){
        //initial account balance
        accountDetails.put("123456789",new BigDecimal("1000.0"));
        accountDetails.put("111111111",new BigDecimal("1000.0"));
        accountDetails.put("222222222",new BigDecimal("1000.0"));
        accountDetails.put("333333333",new BigDecimal("1000.0"));
        accountDetails.put("444444444",new BigDecimal("1000.0"));

        //initial money inside atm
        bankNotes.put(Banknote.FIFTY_JOD , new BigDecimal("10"));
        bankNotes.put(Banknote.TWENTY_JOD , new BigDecimal("20"));
        bankNotes.put(Banknote.FIVE_JOD ,new BigDecimal("100"));
        bankNotes.put(Banknote.TEN_JOD ,new BigDecimal("100"));





    }

    @Override
    public List<Banknote> withdraw(String accountNumber, BigDecimal amount) {
        if(accountDetails.containsKey(accountNumber)){
            //check if account exists
                if(getAccountBalance(accountNumber).compareTo(amount)>=0)
                {
                    //check account balance
                    if(getTotalAmtInATM().compareTo(amount)>=0){
                        //check atm amount
                        debitAccount(accountNumber,amount);
                        return getBankNotes(amount);
                    }
                    else {
                        throw  new NotEnoughMoneyInATMException();
                    }
                }
                else {
                    throw new InsufficientFundsException();
                }
        }
        else
        {
            throw new AccountNotFoundException();
        }

    }

    @Override
    public BigDecimal getAccountBalance(String accountNumber) {
        return accountDetails.get(accountNumber);
    }

    @Override
    public void debitAccount(String accountNumber, BigDecimal amount) {
        //subtract amount from account on debit call
        accountDetails.put(accountNumber ,accountDetails.get(accountNumber).subtract(amount));

    }
   //method to get total amount in atm;foreach to add value;amount in atm returned as totalAmount
    public BigDecimal getTotalAmtInATM(){
        BigDecimal totalAmount = new BigDecimal("0");
        for (Banknote banknote: bankNotes.keySet()
             ) {

            totalAmount = totalAmount.add(banknote.getValue().multiply(bankNotes.get(banknote)));

        }
        return totalAmount;
    }

    //retuns list of banknote
    public List<Banknote> getBankNotes(BigDecimal amount){
        List<Banknote> banknoteList = new ArrayList<>();
        BigDecimal totalAmount = new BigDecimal("0");
        while (amount.compareTo(totalAmount)>0){
            Banknote banknote = getRandomBankNote();
            if (amount.compareTo(totalAmount.add(banknote.getValue()))>=0){
                totalAmount = totalAmount.add(banknote.getValue());
                banknoteList.add(banknote);
                bankNotes.put(banknote,bankNotes.get(banknote).subtract(new BigDecimal("1")));
            }
        }
        return  banknoteList;
    }


    //optional delivery method implemented below
    public Banknote getRandomBankNote(){
        Banknote banknote = Banknote.values()[new Random().nextInt(Banknote.values().length)];
        if (bankNotes.get(banknote).compareTo(BigDecimal.ZERO)==0)
        {
            return getRandomBankNote();
        }
        else return banknote;
    }

}
