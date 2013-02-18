package eventManager.model.pokerTournament;

import javafx.beans.property.SimpleIntegerProperty;

public class SatelitePrizeStruct extends PrizeStruct{

    SimpleIntegerProperty packageAmount = new SimpleIntegerProperty(0);
    int rest;
    SimpleIntegerProperty subpackageAmount = new SimpleIntegerProperty(0);

    public SatelitePrizeStruct() {
        name = "Satelite Tournament";     
    }

    public void setPackageAmount(int packages) {
        this.packageAmount.set(packages);
    }

    public void setSubpackageAmount(int subpackages) {
        this.subpackageAmount.set(subpackages);
    }

    public SimpleIntegerProperty packageAmountProperty() {
        return packageAmount;
    }

    public SimpleIntegerProperty subpackageAmountProperty() {
        return subpackageAmount;
    }
    
    
    
    public void calculatePlayersWhoReceive() {
        playersWhoRecieve = handout/packageAmount.get();
        rest = handout % packageAmount.get();
    }

    @Override
    public void calculatePrizes() {
        calculatePlayersWhoReceive();
        percentagesList.clear();
        int i;
        for (i = 0; i < playersWhoRecieve; i++) {
            percentagesList.add(new PrizePosition(i + 1, 0.0, 0.0, packageAmount.get()));
        }
        if (subpackageAmount.get() != 0) {
            int x = rest / subpackageAmount.get();
            int y = rest % subpackageAmount.get();
            for (int j = 0; j < x; j++) {
                playersWhoRecieve++;
                percentagesList.add(new PrizePosition(percentagesList.size()+1, 0.0, 0.0, subpackageAmount.get()));
            }
            if (y != 0)
                percentagesList.add(new PrizePosition(percentagesList.size()+1, 0.0, 0.0, y));
        } else {
            playersWhoRecieve++;
            percentagesList.add(new PrizePosition(percentagesList.size()+1, 0.0, 0.0, rest));
        }
    }
}
