package ru.job4j.store;

import ru.job4j.model.Account;

import java.util.Collection;

public interface StoreAccount {
    void saveAccount(Account acc);
    void deleteAccount(int id);
    Account findAccountById(int id);
    Account findAccountByName(String name);
    Collection<Account> findAllAccount();
}
