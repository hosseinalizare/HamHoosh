package com.example.koohestantest1.classDirectory;

public class EditProfileField {
    public boolean canValuNull;
    public BaseCodeClass.CompanyEnum Cenum;
    public boolean EditAble;
    public String Explain;
    public String Value;
    public String Description;
    public BaseCodeClass.variableType type;
    public BaseCodeClass.editMode mode;




    public EditProfileField(boolean canValuNull, BaseCodeClass.CompanyEnum cenum, boolean editAble, String explain, String value, String description, BaseCodeClass.variableType type, BaseCodeClass.editMode mode) {
        this.canValuNull = canValuNull;
        Cenum = cenum;
        EditAble = editAble;
        Explain = explain;
        Value = value;
        Description = description;
        this.type = type;
        this.mode = mode;
    }

    public EditProfileField() {
    }

    public boolean isCanValuNull() {
        return canValuNull;
    }

    public void setCanValuNull(boolean canValuNull) {
        this.canValuNull = canValuNull;
    }

    public BaseCodeClass.CompanyEnum getCenum() {
        return Cenum;
    }

    public void setCenum(BaseCodeClass.CompanyEnum cenum) {
        Cenum = cenum;
    }

    public boolean isEditAble() {
        return EditAble;
    }

    public void setEditAble(boolean editAble) {
        EditAble = editAble;
    }

    public String getExplain() {
        return Explain;
    }

    public void setExplain(String explain) {
        Explain = explain;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
