package Service;
import Domain.Listener;
import Domain.Song;
import Domain.Subscription;
import Repository.IRepository;

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
     */
    public SubscriptionService(IRepository<Subscription> subscriptionRepository) {
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
     */

    public void upgradeSubscription(Listener listener, String newType) {
        // Verificăm dacă utilizatorul are deja un abonament
        Subscription currentSubscription = listener.getSubscription();

        if (currentSubscription == null) {
            System.out.println("No existing subscription found. Please subscribe to upgrade.");
            return;
        }

        String currentType = currentSubscription.getType().toLowerCase();
        String targetType = newType.toLowerCase();

        if (currentType.equals(targetType)) {
            System.out.println("You already have the " + newType + " subscription.");
            return;
        }

        switch (targetType) {
            case "premium":
                if (currentType.equals("basic")) {
                    currentSubscription.setType("premium");
                    currentSubscription.setPrice(PREMIUM_PRICE);
                    System.out.println("Subscription upgraded to Premium at price " + PREMIUM_PRICE + ".");
                } else {
                    System.out.println("Invalid upgrade. You cannot upgrade to the requested type.");
                }
                break;
            case "basic":
                if (currentType.equals("premium")) {
                    currentSubscription.setType("basic");
                    currentSubscription.setPrice(BASIC_PRICE);
                    System.out.println("Subscription downgraded to Basic at price " + BASIC_PRICE + ".");
                } else {
                    System.out.println("Invalid downgrade. You cannot downgrade to the requested type.");
                }
                break;
            default:
                System.out.println("Invalid subscription type. Please choose 'basic' or 'premium'.");
        }
    }

    // Crearea unui abonament pentru un listener
    public void createSubscription(Listener listener, String subscriptionType) {
        // Verificăm dacă tipul abonamentului este valid
        if (!subscriptionType.equals("basic") && !subscriptionType.equals("premium")) {
            System.out.println("Invalid subscription type.");
            return;
        }

        // Creăm abonamentul cu tipul și prețul corect
        float price = (subscriptionType.equals("basic")) ? 9.99f : 14.99f;
        Subscription subscription = new Subscription(subscriptionType, price, listener);

        // Salvăm abonamentul în repository
        subscriptionRepository.create(subscription);

        // Asociem abonamentul cu listener-ul
        listener.setSubscription(subscription);

        // Confirmare
        System.out.println("Subscription created successfully for " + listener.getName() + " as " + subscriptionType+"!");
}
    /**
     * Cancels the subscription of a listener (user).
     * If the listener has an active subscription, this method deletes the subscription from
     * the repository and sets the listener's subscription to null.
     * If no subscription is found or the subscription ID is invalid, an appropriate message is displayed.
     *
     * @param listener The listener whose subscription is to be canceled.
     * @return Returns true if the subscription is successfully canceled, otherwise false.
     */
    public boolean cancelSubscription(Listener listener) {
        Subscription subscription = listener.getSubscription();
        if (subscription != null) {
            int subscriptionId = subscription.getId();
            if (subscriptionId != 0) {
                subscriptionRepository.delete(subscriptionId);  // Șterge abonamentul
                listener.setSubscription(null);  // Anulează abonamentul pentru listener
                System.out.println("Subscription canceled for " + listener.getName());
                return true;  // Returnează true pentru a indica succesul
            } else {
                System.out.println("Invalid subscription ID.");
            }
        } else {
            System.out.println("No subscription found for " + listener.getName());
        }
        return false;  // Dacă nu a fost anulat, returnează false
    }
}