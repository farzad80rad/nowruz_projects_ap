/**
 * define a card with ability to do something. contains (skip , draw ,wilds and revers cards)
 */
public interface ActionCard {
    /**
     * do an specific action .
     * @param gameManager game manager of this game.
     */
    void action (GameManager gameManager);
}
