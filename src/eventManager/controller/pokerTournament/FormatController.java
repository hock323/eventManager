package eventManager.controller.pokerTournament;

import eventManager.model.pokerTournament.Addon;
import eventManager.model.pokerTournament.Knockout;
import eventManager.model.pokerTournament.ReBuy;
import eventManager.model.pokerTournament.Reentry;
import eventManager.model.pokerTournament.Tournament;
import eventManager.model.pokerTournament.TournamentConfiguration;
import java.net.URL;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class FormatController  implements Initializable{
    
    private NumberFormat numberFormat = new NumberFormat() {
        private static final long serialVersionUID = 1L;
        @Override
        public StringBuffer format(double d, StringBuffer sb, FieldPosition fp) {
            StringBuffer s = new StringBuffer();
            s.append(Double.toString(d));
            return s;
        }

        @Override
        public StringBuffer format(long l, StringBuffer sb, FieldPosition fp) {
            StringBuffer s = new StringBuffer();
            s.append(Long.toString(l));
            return s;
        }

        @Override
        public Number parse(String string, ParsePosition pp) {
            try {
                int x = Integer.parseInt(string);
                if (x >= 0) {
                    return x;
                } else {
                    return 0;
                }
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    };
    private SimpleBooleanProperty buyinChipsCheck = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty buyinCostCheck = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty numberExceptionCheck = new SimpleBooleanProperty(true);
    private SimpleBooleanProperty formatCheck = new SimpleBooleanProperty(false);
    @FXML TextField costBuyinField;
    @FXML TextField chipsBuyinField;
    @FXML TextField lvMaxBuyinField;
    @FXML TextField rebuyCostField;
    @FXML TextField rebuyChipsField;
    @FXML TextField numMaxRebuyField;
    @FXML TextField levelMaxRebuyField;
    @FXML TextField numMaxReentradaField;
    @FXML TextField chipsAddonField;
    @FXML TextField costAddonField;
    @FXML TextField numMaxAddonField;
    @FXML TextField costKnockoutField;
    @FXML TextField extOrganizationFeeField;
    @FXML TextField leaguePoolField;
    @FXML TextField organizationFeeField;
    @FXML ToggleButton toggleRebuy;
    @FXML ToggleButton toggleAddon;
    @FXML ToggleButton toggleKnockout;
    @FXML ToggleButton entryFee;
    @FXML ToggleButton externalFee;
    @FXML ToggleButton poolFee;
    @FXML Button feeTypeButton;
    @FXML VBox formatVBox;
    @FXML Label perExternalFee;
    @FXML Label perLeaguePool;
    @FXML Label euroRebuy;
    @FXML Label euroAddon;
    @FXML Label euroKnockout;
    private Tournament tournament;
    private ResourceBundle resourceBundle;
    private SimpleBooleanProperty tournamentChanged;
    private SimpleBooleanProperty createMode;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resourceBundle = rb;
        rebuyCostField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                try {
                    costAddonField.setText(rebuyCostField.getText());
                } catch (Exception e) {
                    costAddonField.setText("");
                }
            }
        });
        rebuyChipsField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                try {
                    int chips = Integer.parseInt(rebuyChipsField.getText()) * 2;
                    chipsAddonField.setText(chips + "");
                } catch (NumberFormatException e) {
                    chipsAddonField.setText("");
                }
            }
        });


    }

    public void setTournamentChanged(SimpleBooleanProperty tournamentChanged) {
        this.tournamentChanged = tournamentChanged;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public SimpleBooleanProperty formatCheckProperty() {
        return formatCheck;
    }

    public void setCreateMode(SimpleBooleanProperty createMode) {
        this.createMode = createMode;
    }

    public void bind() {
        lvMaxBuyinField.textProperty().bindBidirectional(tournament.getTournamentConfiguration().getBuyin().maxLevelBuyinProperty(), numberFormat);
        extOrganizationFeeField.textProperty().bindBidirectional(tournament.getTournamentConfiguration().getFees().externalOrganizationProperty(), numberFormat);
        leaguePoolField.textProperty().bindBidirectional(tournament.getTournamentConfiguration().getFees().leaguePoolProperty(), numberFormat);
        costBuyinField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                try {
                    int cost = Integer.parseInt(t1);
                    if (cost > 0) {
                        tournament.getTournamentConfiguration().getBuyin().setCost(cost);
                        if (tournament.getTournamentConfiguration().getReentrada() != null) {
                            tournament.getTournamentConfiguration().getReentrada().setCost(cost);
                        }
                        buyinCostCheck.set(true);
                    } else {
                        tournament.getTournamentConfiguration().getBuyin().setCost(0);
                        buyinCostCheck.set(false);
                    }
                } catch (NumberFormatException e) {
                    tournament.getTournamentConfiguration().getBuyin().setCost(0);
                    buyinCostCheck.set(false);
                }
                checkingFormat();
            }
        });
        chipsBuyinField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                try {
                    int chips = Integer.parseInt(t1);
                    if (chips > 0) {
                        tournament.getTournamentConfiguration().getBuyin().setChips(chips);
                        if (tournament.getTournamentConfiguration().getReentrada() != null) {
                            tournament.getTournamentConfiguration().getReentrada().setChips(chips);
                        }
                        buyinChipsCheck.set(true);
                    } else {
                        tournament.getTournamentConfiguration().getBuyin().setChips(0);
                        buyinChipsCheck.set(false);
                    }
                } catch (NumberFormatException e) {
                    tournament.getTournamentConfiguration().getBuyin().setChips(0);
                    buyinChipsCheck.set(false);
                }
                checkingFormat();
            }
        });
        lvMaxBuyinField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                try {
                    int level = Integer.parseInt(t1);
                    if (level > 0) {
                        tournament.getTournamentConfiguration().getBuyin().setMaxLevelBuyin(Integer.parseInt(t1));
                        if (tournament.getTournamentConfiguration().getReentrada() != null) {
                            tournament.getTournamentConfiguration().getReentrada().setMaxLevel(Integer.parseInt(t1));
                        }
                    } else {
                        tournament.getTournamentConfiguration().getBuyin().setMaxLevelBuyin(0);
                    }
                    numberExceptionCheck.set(true);
                } catch (NumberFormatException e) {
                    tournament.getTournamentConfiguration().getBuyin().setMaxLevelBuyin(0);
                    numberExceptionCheck.set(false);
                }
                checkingFormat();
            }
        });
        numMaxReentradaField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if (!"".equals(numMaxReentradaField.getText())) {
                    try {
                        int max = Integer.parseInt(t1);
                        int chips = tournament.getTournamentConfiguration().getBuyin().getChips();
                        int cost = tournament.getTournamentConfiguration().getBuyin().getMaxLevelBuyin();
                        int maxLevel = tournament.getTournamentConfiguration().getBuyin().getMaxLevelBuyin();
                        if (max > 0) {
                            if (tournament.getTournamentConfiguration().getReentrada() != null) {
                                tournament.getTournamentConfiguration().getReentrada().setCost(cost);
                                tournament.getTournamentConfiguration().getReentrada().setChips(chips);
                                tournament.getTournamentConfiguration().getReentrada().setMaxLevel(maxLevel);
                                tournament.getTournamentConfiguration().getReentrada().setMaxReentrada(max);
                            } else {
                                Reentry reentry = new Reentry(cost, chips);
                                reentry.setMaxReentrada(max);
                                reentry.setMaxLevel(maxLevel);
                                tournament.getTournamentConfiguration().setReentrada(reentry);
                            }
                        } else {
                            tournament.getTournamentConfiguration().setReentrada(null);
                        }
                        numberExceptionCheck.set(true);
                    } catch (NumberFormatException e) {
                        tournament.getTournamentConfiguration().setReentrada(null);
                        if (!"".equals(t1)) {
                            numberExceptionCheck.set(false);
                        }
                    }
                }
                checkingFormat();
            }
        });
        rebuyCostField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                checkRebuy();
            }
        });
        rebuyChipsField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                checkRebuy();
            }
        });
        numMaxRebuyField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                checkRebuy();
            }
        });
        levelMaxRebuyField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                checkRebuy();
            }
        });
        costAddonField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                checkAddon();
            }
        });
        chipsAddonField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                checkAddon();
            }
        });
        numMaxAddonField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                checkAddon();
            }
        });
        costKnockoutField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if (!"".equals(costKnockoutField.getText())) {
                    try {
                        int cost = Integer.parseInt(t1);
                        if (cost > 0) {
                            if (tournament.getTournamentConfiguration().getKnockout() != null) {
                                tournament.getTournamentConfiguration().setKnockout(new Knockout(cost));
                            } else {
                                tournament.getTournamentConfiguration().setKnockout(new Knockout(cost));
                            }
                        } else {
                            tournament.getTournamentConfiguration().setKnockout(null);
                        }
                        numberExceptionCheck.set(true);
                    } catch (NumberFormatException e) {
                        tournament.getTournamentConfiguration().setKnockout(null);
                        if (!"".equals(t1)) {
                            numberExceptionCheck.set(false);
                        }
                    }
                }
                checkingFormat();
            }
        });     
        organizationFeeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                try {
                    if (Integer.parseInt(t1) >= 0) {
                        tournament.getTournamentConfiguration().getFees().setOrganizationFee(Integer.parseInt(t1));
                        numberExceptionCheck.set(true);
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    tournament.getTournamentConfiguration().getFees().setOrganizationFee(0);
                    if (!"".equals(t1))
                        numberExceptionCheck.set(false);
                }
                checkingFormat();
            }
        });
    }
    
    public void loadingView() {
        TournamentConfiguration tc = tournament.getTournamentConfiguration();
        chipsBuyinField.setText(tc.getBuyin().getChips() + "");
        costBuyinField.setText(tc.getBuyin().getCost() + "");
        if (tc.getBuyin().getMaxLevelBuyin() != null) {
            lvMaxBuyinField.setText(tc.getBuyin().getMaxLevelBuyin() + "");
        }
        if (tc.getRebuy() != null) {
            toggleRebuy.selectedProperty().set(true);
            rebuyChipsField.setText(tc.getRebuy().getChips() + "");
            rebuyCostField.setText(tc.getRebuy().getCost() + "");
            numMaxRebuyField.setText(tc.getRebuy().getMaxRebuy() + "");
            levelMaxRebuyField.setText(tc.getRebuy().getMaxLevel() + "");
        }
        if (tc.getAddon() != null) {
            toggleAddon.selectedProperty().set(true);
            chipsAddonField.setText(tc.getAddon().getChips() + "");
            numMaxAddonField.setText(tc.getAddon().getMaxAddons() + "");
        }
        if (tc.getReentrada() != null) {
            numMaxReentradaField.setText(tc.getReentrada().getMaxReentrada() + "");
        }
        if (tc.getKnockout() != null) {
            toggleKnockout.selectedProperty().set(true);
            costKnockoutField.setText(tc.getKnockout().getCost() + "");
        }
        if (tc.getFees() != null) {
            entryFee.selectedProperty().set(true);
            organizationFeeField.setText(tc.getFees().getOrganizationFee() + "");
            if (tc.getFees().getExternalOrganization() != 0) {
                extOrganizationFeeField.setText(tc.getFees().getExternalOrganization() + "");
                externalFee.selectedProperty().set(true);
                extOrganizationFeeField.setDisable(false);
            }
            if (tc.getFees().getLeaguePool() != 0) {
                leaguePoolField.setText(tc.getFees().getLeaguePool() + "");
                poolFee.selectedProperty().set(true);
                leaguePoolField.setDisable(false);
            }
        }
    }
    
    public void checkRebuy() {
        try {
            if (!"".equals(rebuyCostField.getText()) && !"".equals(rebuyChipsField.getText())) {
                int cost = Integer.parseInt(rebuyCostField.getText());
                int chips = Integer.parseInt(rebuyChipsField.getText());
                int max = 0, maxLevel = 0;
                if (!"".equals(numMaxRebuyField.getText())) {
                    max = Integer.parseInt(numMaxRebuyField.getText());
                }
                if (!"".equals(numMaxRebuyField.getText())) {
                    maxLevel = Integer.parseInt(levelMaxRebuyField.getText());
                }
                if (cost != 0 && chips != 0) {
                    if (tournament.getTournamentConfiguration().getRebuy() != null) {
                        tournament.getTournamentConfiguration().getRebuy().setCost(cost);
                        tournament.getTournamentConfiguration().getRebuy().setChips(chips);
                        tournament.getTournamentConfiguration().getRebuy().setMaxLevel(maxLevel);
                        tournament.getTournamentConfiguration().getRebuy().setMaxRebuy(max);
                    } else {
                        tournament.getTournamentConfiguration().setRebuy(new ReBuy(cost, chips));
                        tournament.getTournamentConfiguration().getRebuy().setMaxRebuy(max);
                        tournament.getTournamentConfiguration().getRebuy().setMaxLevel(maxLevel);
                    }
                }
            } else {
                tournament.getTournamentConfiguration().setRebuy(null);
            }
        } catch (NumberFormatException e) {
            tournament.getTournamentConfiguration().setRebuy(null);
            if (!"".equals(rebuyCostField.getText()) || !"".equals(rebuyChipsField.getText()))
                numberExceptionCheck.set(false);
        }
    }
    
    public void checkAddon() {
        try {
            if (!"".equals(costAddonField.getText()) && "".equals(chipsAddonField.getText())) {
                int cost = Integer.parseInt(costAddonField.getText());
                int chips = Integer.parseInt(chipsAddonField.getText());
                int max = 0, maxLevel = 0;
                if (!"".equals(numMaxRebuyField.getText())) {
                    max = Integer.parseInt(numMaxRebuyField.getText());
                }
                if (!"".equals(numMaxRebuyField.getText())) {
                    maxLevel = Integer.parseInt(levelMaxRebuyField.getText());
                }
                if (cost != 0 && chips != 0) {
                    if (tournament.getTournamentConfiguration().getAddon() != null) {
                        tournament.getTournamentConfiguration().getAddon().setCost(cost);
                        tournament.getTournamentConfiguration().getAddon().setChips(chips);
                        tournament.getTournamentConfiguration().getAddon().setMaxLevel(maxLevel);
                        tournament.getTournamentConfiguration().getAddon().setMaxAddons(max);
                    } else {
                        Addon addon = new Addon(cost, chips);
                        addon.setMaxLevel(maxLevel);
                        addon.setMaxAddons(max);
                        tournament.getTournamentConfiguration().setAddon(addon);
                    }
                }
            } else {
                tournament.getTournamentConfiguration().setAddon(null);
            }
        } catch (NumberFormatException e) {
            if (!"".equals(costAddonField.getText()) || !"".equals(chipsAddonField.getText()))
                numberExceptionCheck.set(false);
            tournament.getTournamentConfiguration().setAddon(null);
        }
    }
    
    public void selectInField(MouseEvent e) {
        TextField field = (TextField) e.getSource();
            field.selectAll();
    }

    public void toggleRebuy(ActionEvent e) {
        if (toggleRebuy.isSelected()) {
            rebuyCostField.setDisable(false);
            rebuyChipsField.setDisable(false);
            numMaxRebuyField.setDisable(false);
            levelMaxRebuyField.setDisable(false);
            euroRebuy.setDisable(false);
            costAddonField.setDisable(false);
            chipsAddonField.setDisable(false);
            numMaxAddonField.setDisable(false);
            toggleAddon.setSelected(true);
            euroAddon.setDisable(false);
        } else {
            rebuyCostField.setDisable(true);
            rebuyChipsField.setDisable(true);
            numMaxRebuyField.setDisable(true);
            levelMaxRebuyField.setDisable(true);
            euroRebuy.setDisable(true);
            rebuyCostField.setText("");
            rebuyChipsField.setText("");
            numMaxRebuyField.setText("");
            levelMaxRebuyField.setText("");
        }
    }

    public void toggleAddOn(ActionEvent e) {
        if (toggleAddon.isSelected()) {
            costAddonField.setDisable(false);
            chipsAddonField.setDisable(false);
            numMaxAddonField.setDisable(false);
            euroAddon.setDisable(false);
        } else {
            costAddonField.setDisable(true);
            chipsAddonField.setDisable(true);
            numMaxAddonField.setDisable(true);
            euroAddon.setDisable(true);
            costAddonField.setText("");
            chipsAddonField.setText("");
            numMaxAddonField.setText("");
        }
    }
    
    public void toggleKnockout(ActionEvent e) {
        if (toggleKnockout.isSelected()) {
            costKnockoutField.setDisable(false);
            euroKnockout.setDisable(false);
        } else {
            costKnockoutField.setDisable(true);
            costKnockoutField.setText("");
            euroKnockout.setDisable(true);
        }
    }

    public void toggleFee(ActionEvent e) {
        ToggleButton toggle = (ToggleButton) e.getSource();
        if (toggle.isSelected()) {
            organizationFeeField.setDisable(false);
            feeTypeButton.setDisable(false);
        } else {
            organizationFeeField.setDisable(true);
            organizationFeeField.setText("0");
            feeTypeButton.setDisable(true);
        }
    }

    public void toggleExtFee(ActionEvent e) {
        ToggleButton toggle = (ToggleButton) e.getSource();
        if (toggle.isSelected()) {
            extOrganizationFeeField.setDisable(false);
            extOrganizationFeeField.selectAll();
            perExternalFee.setDisable(false);
        } else {
            extOrganizationFeeField.setText("0");
            extOrganizationFeeField.setDisable(true);
            perExternalFee.setDisable(true);
        }
    }

    public void togglePool(ActionEvent e) {
        ToggleButton toggle = (ToggleButton) e.getSource();
        if (toggle.isSelected()) {
            leaguePoolField.setDisable(false);
            leaguePoolField.selectAll();
            perLeaguePool.setDisable(false);
        } else {
            leaguePoolField.setText("0");
            leaguePoolField.setDisable(true);
            perLeaguePool.setDisable(true);
        }
    }
    
    public void toggleFeePercentage(ActionEvent e) {
        Button button = (Button) e.getSource();
        if ("%".equals(button.getText())) {
            feeTypeButton.setText("€");
        } else {
            feeTypeButton.setText("%");
        }
    }

    public void setInitedState(){
        formatVBox.setDisable(true);
    }
    
    public void mouseEntered(MouseEvent e) {
        Node node = (Node) e.getSource();
        node.getScene().setCursor(Cursor.HAND);
    }

    public void mouseExited(MouseEvent e) {
        Node node = (Node) e.getSource();
        try {
            node.getScene().setCursor(Cursor.DEFAULT);
        } catch (NullPointerException w) {
        }
    }

    public void checkingFormat() {
        if (buyinCostCheck.get() && buyinChipsCheck.get() && numberExceptionCheck.get()) {
            formatCheck.set(true);
        } else {
            formatCheck.set(false);
        }
    }

}
