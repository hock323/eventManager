package eventManager.fx;

import eventManager.model.pokerTournament.Announcement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;

public class EditingBooleanCell extends TableCell<Announcement, Boolean> {

    private CheckBox checkBox;

    public EditingBooleanCell() {
        checkBox = new CheckBox();
        checkBox.setDisable(true);
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (isEditing()) {
                    commitEdit(newValue == null ? false : newValue);
                }
            }
        });
        this.setGraphic(checkBox);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setEditable(true);
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (isEmpty()) {
            return;
        }
        checkBox.setDisable(false);
        checkBox.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        checkBox.setDisable(true);
    }

    @Override
    public void commitEdit(Boolean value) {
        super.commitEdit(value);
        checkBox.setDisable(true);
    }

    @Override
    public void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        this.setAlignment(Pos.CENTER);
        paintCell();
    }
    
    private void paintCell() {
    if (checkBox == null) {
        checkBox = new CheckBox();
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                setItem(new_val);
                        getTableView().getItems().get(getTableRow().getIndex()).setActive(new_val);
            }
        });
    }
    checkBox.setSelected(getValue());
    setText(null);
    setGraphic(checkBox);
}

private Boolean getValue() {
    return getItem() == null ? false : getItem();
}

}
