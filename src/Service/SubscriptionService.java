package Service;
import Domain.Listener;
import Domain.Song;
import Domain.Subscription;
import Repository.IRepository;

import Exceptions.BusinessLogicException;
import Exceptions.EntityNotFoundException;
import Exceptions.ValidationException;

/**
 * SubscriptionService is responsible for managing and updating user subscriptions.
 * It allows users to upgrade, downgrade, and cancel their subscriptions.
 * The service interacts with the subscription repository to store and manage subscription data.
 */
public class SubscriptionService {
    private static final float BASIC_PRICE = 9.99f;
    private static final float PREMIUM_PRICE = 14.99f;
    private final IRepository<Subscription> subscriptionRepository;

    /**
     * Constructor to initialize SubscriptionService with a given subscription repository.
     *
     * @param subscriptionRepository The repository used to store and manage subscriptions.
     * @throws ValidationException if the repository is null.
     */
    public SubscriptionService(IRepository<Subscription> subscriptionRepository) {
        if (subscriptionRepository == null) {
            throw new ValidationException("Subscription repository cannot be null.");
        }
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Upgrades or downgrades the subscription type for a listener (user).
     * If the listener has an existing subscription, this method updates the subscription
     * to the new type (either "premium" or "basic").
     * If the listener does not have an existing subscription or tries to upgrade/downgrade incorrectly,
     * an appropriate message is displayed.
     *
     * @param listener The listener whose subscription is to be updated.
     * @param newType The type to upgrade or downgrade the subscription to ("basic" or "premium").
     * @throws EntityNotFoundException if the listener does not have a subscription.
     * @throws ValidationException if the newType is invalid.
     */
    public void upgradeSubscription(Listener listener, String newType) {
        if (newType == null || newType.trim().isEmpty()) {
            throw new ValidationException("Subscription type cannot be null or empty.");
        }

        Subscription currentSubscription = listener.getSubscription();

        if (currentSubscription == null) {
            throw new EntityNotFoundException("No existing subscription found. Please subscribe to upgrade.");
        }

        String currentType = currentSubscription.getType().toLowerCase();
        String targetType = newType.toLowerCase();

        if (currentType.equals(targetType)) {
            throw new BusinessLogicException("You already have the " + newType + " subscription.");
        }

        switch (targetType) {
            case "premium":
                if (currentType.equals("basic")) {
                    currentSubscription.setType("premium");
                    currentSubscription.setPrice(PREMIUM_PRICE);
                    System.out.println("Subscription upgraded to Premium at price " + PREMIUM_PRICE + ".");
                } else {
                    throw new BusinessLogicException("Invalid upgrade. You cannot upgrade to the requested type.");
                }
                break;
            case "basic":
                if (currentType.equals("premium")) {
                    currentSubscription.setType("basic");
                    currentSubscription.setPrice(BASIC_PRICE);
                    System.out.println("Subscription downgraded to Basic at price " + BASIC_PRICE + ".");
                } else {
                    throw new BusinessLogicException("Invalid downgrade. You cannot downgrade to the requested type.");
                }
                break;
            default:
                throw new ValidationException("Invalid subscription type. Please choose 'basic' or 'premium'.");
        }
    }

    /**
     * Creates a subscription for a listener based on the provided username and subscription type.
     * @param listener The listener who will be assigned the subscription.
     * @param subscriptionType The type of subscription to be created for the listener (e.g., "Premium" or "Basic").
     * @throws ValidationException if the subscription type is invalid.
     */
    public void createSubscription(Listener listener, String subscriptionType) {
        if (subscriptionType == null || subscriptionType.trim().isEmpty()) {
            throw new ValidationException("Subscription type cannot be null or empty.");
        }

        if (!subscriptionType.equals("basic") && !subscriptionType.equals("premium")) {
            throw new ValidationException("Invalid subscription type.");
        }

        float price = (subscriptionType.equals("basic")) ? BASIC_PRICE : PREMIUM_PRICE;
        Subscription subscription = new Subscription(subscriptionType, price, listener);
        subscriptionRepository.create(subscription);
        listener.setSubscription(subscription);
        System.out.println("Subscription created successfully for " + listener.getName() + " as " + subscriptionType + "!");
    }
    /**
     * Cancels the subscription of a listener (user).
     * If the listener has an active subscription, this method deletes the subscription from
     * the repository and sets the listener's subscription to null.
     * If no subscription is found or the subscription ID is invalid, an appropriate message is displayed.
     *
     * @param listener The listener whose subscription is to be canceled.
     * @return Returns true if the subscription is successfully canceled, otherwise false.
     * @throws EntityNotFoundException if no active subscription is found for the listener.
     */
    public boolean cancelSubscription(Listener listener) {
        Subscription subscription = listener.getSubscription();

        if (subscription == null) {
            throw new EntityNotFoundException("No subscription found for " + listener.getName());
        }

        int subscriptionId = subscription.getId();
        if (subscriptionId != 0) {
            subscriptionRepository.delete(subscriptionId);
            listener.setSubscription(null);
            System.out.println("Subscription canceled for " + listener.getName());
            return true;
        } else {
            throw new BusinessLogicException("Invalid subscription ID.");
        }
    }
}
