package org.example.model;

public class Session {
    private static User currentUser;
    private static Customer currentCustomer;
    private static Integer currentDraftId;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public static void setCurrentCustomer(Customer customer) {
        currentCustomer = customer;
    }

    public static Integer getCurrentDraftId() { return currentDraftId; }
    public static void setCurrentDraftId(Integer id) { currentDraftId = id; }
    public static void clearCurrentDraftId() { currentDraftId = null; }
}