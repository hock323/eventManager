package eventManager.model.pokerTournament;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChopPrizes {

    protected ObservableList<ChopPosition> chopsList = FXCollections.observableArrayList(new ArrayList<ChopPosition>());
    int totalChips = 0;
    PrizeStruct prizeStruct;

    public ChopPrizes(PrizeStruct prizeStruct) {
        this.prizeStruct = prizeStruct;
    }

    public ChopPrizes() {
    }

    @XmlElement
    public PrizeStruct getPrizeStruct() {
        return prizeStruct;
    }

    public void setPrizeStruct(PrizeStruct prizeStruct) {
        this.prizeStruct = prizeStruct;
    }

    public void add(ChopPosition chop) {
        chopsList.add(chop);
        calculate();
    }

    public ObservableList<ChopPosition> getChopsList() {
        return chopsList;
    }

    public void setTotalChips(int totalChips) {
        this.totalChips = totalChips;
        calculate();
    }

    private void calculateSimplifiedChops() {
        if (chopsList.size() >= 2 && prizeStruct.getPercentagesList().size() >= chopsList.size()) {
            int totalPrizes = 0;
            int basePrize = prizeStruct.getPercentagesList().get(chopsList.size() - 1).getMoney();
            double percentageChips;
            for (int i = 0; i < chopsList.size(); i++) {
                totalPrizes += prizeStruct.getPercentagesList().get(i).getMoney();
            }
            double rest = totalPrizes - basePrize * chopsList.size();
            for (int i = 0; i < chopsList.size(); i++) {
                percentageChips = (double) chopsList.get(i).getChips() / (double) totalChips;
                int prize = (int) (basePrize + rest * percentageChips);
                chopsList.get(i).setSimple(prize);
            }
            int pos = 0, money = 0;
            for (int i = 0; i < chopsList.size(); i++) {
                money += chopsList.get(i).getSimple();
            }
            for (int i = 1; i < chopsList.size(); i++) {
                if (chopsList.get(i).getSimple() >= chopsList.get(i - 1).getSimple()) {
                    pos = i;
                }
            }
            chopsList.get(pos).setSimple(chopsList.get(pos).getSimple() + (totalPrizes - money));
        }
    }

    private void calculateICMChops() {
        if (chopsList.size() >= 2 && prizeStruct.getPercentagesList().size() >= chopsList.size()) {
            IcmAlgorithm sicm = new IcmAlgorithm();
            double[] payouts = new double[chopsList.size()];
            double[] stacks = new double[chopsList.size()];
            int totalPrizes = 0;
            for (int i = 0; i < chopsList.size(); i++) {
                stacks[i] = chopsList.get(i).getChips();
            }
            for (int i = 0; i < chopsList.size(); i++) {
                payouts[i] = prizeStruct.getPercentagesList().get(i).getMoney();
                totalPrizes += prizeStruct.getPercentagesList().get(i).getMoney();
            }
            double[] eqs = sicm.getEquities(payouts, stacks, 10000);
            int x = 0;
            for (int i = 0; i < chopsList.size(); i++) {
                x += (int) eqs[i];
                chopsList.get(i).setIcm((int) eqs[i]);
            }
            chopsList.get(0).setIcm(chopsList.get(0).getIcm() + (totalPrizes - x));
        }
    }

    public void calculate() {
        calculateSimplifiedChops();
        calculateICMChops();
    }

    public class IcmAlgorithm {

        private Random rnd = new Random();

        public double[] getEquities(double[] payouts, double[] stacks,
                int maxIterations) {
            if (payouts.length > stacks.length) {
                payouts = Arrays.copyOf(payouts, stacks.length);
            }

            double[] equities = new double[stacks.length];
            double[] exp = getExponents(stacks);
            final double[] r = new double[stacks.length];

            Integer[] ids = new Integer[stacks.length];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = i;
            }
            Comparator<Integer> comp = new Comparator<Integer>() {
                @Override
                public int compare(Integer i1, Integer i2) {
                    if (r[i1] < r[i2]) {
                        return 1;
                    }
                    if (r[i1] > r[i2]) {
                        return -1;
                    }
                    return 0;
                }
            };
            for (int iteration = 0; iteration < maxIterations; iteration++) {
                for (int i = 0; i < r.length; i++) {
                    r[i] = Math.pow(rnd.nextDouble(), exp[i]);
                }
                Arrays.sort(ids, comp);
                for (int i = 0; i < payouts.length; i++) {
                    equities[ids[i]] += payouts[i];
                }
            }
            for (int i = 0; i < equities.length; i++) {
                equities[i] /= (double) maxIterations;
            }
            return equities;
        }

        private double[] getExponents(double[] stacks) {
            double t = 0;
            for (double s : stacks) {
                t += s;
            }
            t /= (double) stacks.length;
            double[] exp = new double[stacks.length];
            for (int i = 0; i < stacks.length; i++) {
                exp[i] = t / stacks[i];
            }
            return exp;
        }
    }
}
