package com.poolingpeople.jmeter.setup;


/**
 * Created by hendrik on 27.05.15.
 */
public class WorkspaceItem {

    String referencedItemUuid, text;
    int[] places;

    /**
     *
     * @param referencedItemUuid
     *          The element on witch this wsi should reference
     * @param amountPlaces
     *          How many column / row pairs have to be available
     */
    public WorkspaceItem(String referencedItemUuid, int amountPlaces) {
        this.referencedItemUuid = referencedItemUuid;
        text = "My text is " + DataGenerator.getRandomString(5, DataGenerator.CHARACTERS);
        places = new int[amountPlaces];
        for(int i = 0; i < amountPlaces; i++) {
            int value;
            do {
                value = (int)(Math.random() * 20);
            } while(isUsedInArray(places, value));

            places[i] = value;
        }
    }

    private boolean isUsedInArray(int[] places, int value) {
        for(int i = 0; i < places.length; i++) {
            if(places[i] == value) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(referencedItemUuid);
        sb.append(",").append(text);
        for(int i = 0; i < places.length; i++) {
            sb.append(",").append(places[i]);
        }
        sb.append(System.lineSeparator());
        return sb.toString();
    }

}
