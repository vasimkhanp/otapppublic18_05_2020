package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Agent implements Parcelable {

    @SerializedName("status")
    int Status;
    @SerializedName("message")
    String sMessage;
    @SerializedName("agent_id")
    String sAgentId;
    @SerializedName("agent_name")
    String sAgentName;
    @SerializedName("agent_currency")
    String sAgentCurrency;
    @SerializedName("is_granted_for_tkt_selling")
    String sIsGrantedForTicketSelling;
    @SerializedName("is_granted_for_stnd_tkts")
    String sIsGrantedForStandingTicket;
    @SerializedName("is_granted_for_cargo_tkts")
    String sIsGrantedForCargoTicket;
    @SerializedName("is_granted_for_statements")
    String sIsGrantedForStatements;
    @SerializedName("is_granted_for_agent_summary")
    String sIsGrantedForAgentSummary;
    @SerializedName("is_granted_for_asign_conductor_bus")
    String sIsGrantedForAssignConductorBus;
    @SerializedName("is_granted_for_asign_driver_bus")
    String sIsGrantedForAssignDriverBus;
    @SerializedName("is_granted_for_mechanich_bus")
    String sIsGrantedForMechanicBus;
    @SerializedName("is_granted_to_activate_agent")
    String sIsGrantedForActivateAgent;
    @SerializedName("is_granted_for_prnt_tkts")
    String sIsGrantedForPrintTicket;
    @SerializedName("is_granted_for_psngr_list")
    String sIsGrantedForPassengerList;
    @SerializedName("is_granted_for_cargo_list")
    String sIsGrantedForCargoList;
    @SerializedName("agent_float_bal")
    String sAgentFloatBal;
    @SerializedName("is_granted_to_show_float")
    String sIsGrantedToShowFloat;


    @SerializedName("is_granted_stock_print")
    String sIsGrantedToStockPrint;
    @SerializedName("is_granted_stock_out")
    String sIsGrantedToStockOut;
    @SerializedName("is_granted_stock_in")
    String sIsGrantedToStockIn;
    @SerializedName("is_granted_for_add_expenses")
    String sIsGrantedForAddExpense;
    @SerializedName("is_granted_for_add_incomes")
    String sIsGrantedForAddIncome;

    @SerializedName("agent_username")
    String sAgentUsername;
    @SerializedName("user_type")
    String sUserType;
    @SerializedName("key")
    String sKey;
    @SerializedName("List_exp_inc")
    JsonObject itemArrayList;
    @SerializedName("max_travel_days")
    String sMaxDate;
    @SerializedName("max_stnd_seats")
    String sMaxSeats;
//    @SerializedName("header")
//    String sHeader;
//    @SerializedName("footer")
//    String sFooter;
    String sName = sAgentName;
    String sLoginHistory;
    String sUserId;

    public Agent(int status, String sMessage, String sAgentId, String sAgentName, String sAgentCurrency,
                 String sIsGrantedForTicketSelling, String sIsGrantedForStandingTicket,
                 String sIsGrantedForCargoTicket, String sIsGrantedForStatements,
                 String sIsGrantedForAgentSummary, String sIsGrantedForAssignConductorBus,
                 String sIsGrantedForAssignDriverBus, String sIsGrantedForMechanicBus,
                 String sIsGrantedForActivateAgent, String sIsGrantedForPrintTicket,
                 String sIsGrantedForPassengerList, String sIsGrantedForCargoList,
                 String sAgentFloatBal, String sIsGrantedToShowFloat, String sAgentUsername,
                 String sUserType, String sKey,
                 String sName, String sLoginHistory, String sMaxDate, String sMaxSeats) {
        Status = status;
        this.sMessage = sMessage;
        this.sAgentId = sAgentId;
        this.sAgentName = sAgentName;
        this.sAgentCurrency = sAgentCurrency;
        this.sIsGrantedForTicketSelling = sIsGrantedForTicketSelling;
        this.sIsGrantedForStandingTicket = sIsGrantedForStandingTicket;
        this.sIsGrantedForCargoTicket = sIsGrantedForCargoTicket;
        this.sIsGrantedForStatements = sIsGrantedForStatements;
        this.sIsGrantedForAgentSummary = sIsGrantedForAgentSummary;
        this.sIsGrantedForAssignConductorBus = sIsGrantedForAssignConductorBus;
        this.sIsGrantedForAssignDriverBus = sIsGrantedForAssignDriverBus;
        this.sIsGrantedForMechanicBus = sIsGrantedForMechanicBus;
        this.sIsGrantedForActivateAgent = sIsGrantedForActivateAgent;
        this.sIsGrantedForPrintTicket = sIsGrantedForPrintTicket;
        this.sIsGrantedForPassengerList = sIsGrantedForPassengerList;
        this.sIsGrantedForCargoList = sIsGrantedForCargoList;
        this.sAgentFloatBal = sAgentFloatBal;
        this.sIsGrantedToShowFloat = sIsGrantedToShowFloat;
        this.sAgentUsername = sAgentUsername;
        this.sUserType = sUserType;
        this.sKey = sKey;
        this.sName = sName;
        this.sLoginHistory = sLoginHistory;
        this.sMaxDate = sMaxDate;
        this.sMaxSeats = sMaxSeats;
    }

    public Agent() {

    }

    public Agent(String sAgentId, String sLEmail, String sAgentName, String sAgentCurrency,
                 String sIsGrantedForTicketSelling, String sIsGrantedForStandingTicket, String sIsGrantedForCargoTicket,
                 String sIsGrantedForStatements, String sIsGrantedForAgentSummary, String sIsGrantedForAsignConductorBus,
                 String sIsGrantedForAsignDriverBus, String sIsGrantedForMechanichBus, String sIsGrantedForActivateAgent,
                 String sIsGrantedForPrintTicket, String sIsGrantedForPassengerList, String sIsGrantedForCargoList,
                 String sAgentFloatBal, String sIsGrantedToShowFloat, String sAgentUsername,
                 String sLKey, String sLoginHistory, String sUserType, String sMaxDate) {
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getsMessage() {
        return sMessage;
    }

    public void setsMessage(String sMessage) {
        this.sMessage = sMessage;
    }

    public String getsAgentId() {
        return sAgentId;
    }

    public void setsAgentId(String sAgentId) {
        this.sAgentId = sAgentId;
    }

    public String getsAgentName() {
        return sAgentName;
    }

    public void setsAgentName(String sAgentName) {
        this.sAgentName = sAgentName;
    }

    public String getsAgentCurrency() {
        return sAgentCurrency;
    }

    public void setsAgentCurrency(String sAgentCurrency) {
        this.sAgentCurrency = sAgentCurrency;
    }

    public String getsIsGrantedForTicketSelling() {
        return sIsGrantedForTicketSelling;
    }

    public void setsIsGrantedForTicketSelling(String sIsGrantedForTicketSelling) {
        this.sIsGrantedForTicketSelling = sIsGrantedForTicketSelling;
    }

    public String getsIsGrantedForStandingTicket() {
        return sIsGrantedForStandingTicket;
    }

    public void setsIsGrantedForStandingTicket(String sIsGrantedForStandingTicket) {
        this.sIsGrantedForStandingTicket = sIsGrantedForStandingTicket;
    }

    public String getsIsGrantedForCargoTicket() {
        return sIsGrantedForCargoTicket;
    }

    public void setsIsGrantedForCargoTicket(String sIsGrantedForCargoTicket) {
        this.sIsGrantedForCargoTicket = sIsGrantedForCargoTicket;
    }

    public String getsIsGrantedForStatements() {
        return sIsGrantedForStatements;
    }

    public void setsIsGrantedForStatements(String sIsGrantedForStatements) {
        this.sIsGrantedForStatements = sIsGrantedForStatements;
    }

    public String getsIsGrantedForAgentSummary() {
        return sIsGrantedForAgentSummary;
    }

    public void setsIsGrantedForAgentSummary(String sIsGrantedForAgentSummary) {
        this.sIsGrantedForAgentSummary = sIsGrantedForAgentSummary;
    }

    public String getsIsGrantedForAssignConductorBus() {
        return sIsGrantedForAssignConductorBus;
    }

    public void setsIsGrantedForAssignConductorBus(String sIsGrantedForAssignConductorBus) {
        this.sIsGrantedForAssignConductorBus = sIsGrantedForAssignConductorBus;
    }

    public String getsIsGrantedForAssignDriverBus() {
        return sIsGrantedForAssignDriverBus;
    }

    public void setsIsGrantedForAssignDriverBus(String sIsGrantedForAssignDriverBus) {
        this.sIsGrantedForAssignDriverBus = sIsGrantedForAssignDriverBus;
    }

    public String getsIsGrantedForMechanicBus() {
        return sIsGrantedForMechanicBus;
    }

    public void setsIsGrantedForMechanicBus(String sIsGrantedForMechanicBus) {
        this.sIsGrantedForMechanicBus = sIsGrantedForMechanicBus;
    }

    public String getsIsGrantedForActivateAgent() {
        return sIsGrantedForActivateAgent;
    }

    public void setsIsGrantedForActivateAgent(String sIsGrantedForActivateAgent) {
        this.sIsGrantedForActivateAgent = sIsGrantedForActivateAgent;
    }

    public String getsIsGrantedForPrintTicket() {
        return sIsGrantedForPrintTicket;
    }

    public void setsIsGrantedForPrintTicket(String sIsGrantedForPrintTicket) {
        this.sIsGrantedForPrintTicket = sIsGrantedForPrintTicket;
    }

    public String getsIsGrantedForPassengerList() {
        return sIsGrantedForPassengerList;
    }

    public void setsIsGrantedForPassengerList(String sIsGrantedForPassengerList) {
        this.sIsGrantedForPassengerList = sIsGrantedForPassengerList;
    }

    public String getsIsGrantedForCargoList() {
        return sIsGrantedForCargoList;
    }

    public void setsIsGrantedForCargoList(String sIsGrantedForCargoList) {
        this.sIsGrantedForCargoList = sIsGrantedForCargoList;
    }

    public String getsAgentFloatBal() {
        return sAgentFloatBal;
    }

    public void setsAgentFloatBal(String sAgentFloatBal) {
        this.sAgentFloatBal = sAgentFloatBal;
    }

    public String getsIsGrantedToShowFloat() {
        return sIsGrantedToShowFloat;
    }

    public void setsIsGrantedToShowFloat(String sIsGrantedToShowFloat) {
        this.sIsGrantedToShowFloat = sIsGrantedToShowFloat;
    }

    public String getsAgentUsername() {
        return sAgentUsername;
    }

    public void setsAgentUsername(String sAgentUsername) {
        this.sAgentUsername = sAgentUsername;
    }

    public String getsUserType() {
        return sUserType;
    }

    public void setsUserType(String sUserType) {
        this.sUserType = sUserType;
    }

    public String getsKey() {
        return sKey;
    }

    public void setsKey(String sKey) {
        this.sKey = sKey;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsLoginHistory() {
        return sLoginHistory;
    }

    public void setsLoginHistory(String sLoginHistory) {
        this.sLoginHistory = sLoginHistory;
    }

    public String getsUserId() {
        return sUserId;
    }

    public void setsUserId(String sUserId) {
        this.sUserId = sUserId;
    }

    public String getsMaxDate() {
        return sMaxDate;
    }

    public void setsMaxDate(String sMaxDate) {
        this.sMaxDate = sMaxDate;
    }

    public String getsMaxSeats() {
        return sMaxSeats;
    }

    public void setsMaxSeats(String sMaxSeats) {
        this.sMaxSeats = sMaxSeats;
    }

    public String getsIsGrantedToStockPrint() {
        return sIsGrantedToStockPrint;
    }

    public void setsIsGrantedToStockPrint(String sIsGrantedToStockPrint) {
        this.sIsGrantedToStockPrint = sIsGrantedToStockPrint;
    }

    public String getsIsGrantedToStockOut() {
        return sIsGrantedToStockOut;
    }

    public void setsIsGrantedToStockOut(String sIsGrantedToStockOut) {
        this.sIsGrantedToStockOut = sIsGrantedToStockOut;
    }

    public String getsIsGrantedToStockIn() {
        return sIsGrantedToStockIn;
    }

    public void setsIsGrantedToStockIn(String sIsGrantedToStockIn) {
        this.sIsGrantedToStockIn = sIsGrantedToStockIn;
    }


    public String getsIsGrantedForAddExpense() {
        return sIsGrantedForAddExpense;
    }

    public void setsIsGrantedForAddExpense(String sIsGrantedForAddExpense) {
        this.sIsGrantedForAddExpense = sIsGrantedForAddExpense;
    }

    public String getsIsGrantedForAddIncome() {
        return sIsGrantedForAddIncome;
    }

    public void setsIsGrantedForAddIncome(String sIsGrantedForAddIncome) {
        this.sIsGrantedForAddIncome = sIsGrantedForAddIncome;
    }

    public JsonObject getItemArrayList() {
        return itemArrayList;
    }

    public void setItemArrayList(JsonObject itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

  /*  public String getsFooter() {
        return sFooter;
    }

    public void setsFooter(String sFooter) {
        this.sFooter = sFooter;
    }

    public String getsHeader() {
        return sHeader;
    }

    public void setsHeader(String sHeader) {
        this.sHeader = sHeader;
    }*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Status);
        dest.writeString(this.sMessage);
        dest.writeString(this.sAgentId);
        dest.writeString(this.sAgentName);
        dest.writeString(this.sAgentCurrency);
        dest.writeString(this.sIsGrantedForTicketSelling);
        dest.writeString(this.sIsGrantedForStandingTicket);
        dest.writeString(this.sIsGrantedForCargoTicket);
        dest.writeString(this.sIsGrantedForStatements);
        dest.writeString(this.sIsGrantedForAgentSummary);
        dest.writeString(this.sIsGrantedForAssignConductorBus);
        dest.writeString(this.sIsGrantedForAssignDriverBus);
        dest.writeString(this.sIsGrantedForMechanicBus);
        dest.writeString(this.sIsGrantedForActivateAgent);
        dest.writeString(this.sIsGrantedForPrintTicket);
        dest.writeString(this.sIsGrantedForPassengerList);
        dest.writeString(this.sIsGrantedForCargoList);
        dest.writeString(this.sAgentFloatBal);
        dest.writeString(this.sIsGrantedToShowFloat);
        dest.writeString(this.sIsGrantedToStockPrint);
        dest.writeString(this.sIsGrantedToStockOut);
        dest.writeString(this.sIsGrantedToStockIn);
        dest.writeString(this.sIsGrantedForAddExpense);
        dest.writeString(this.sIsGrantedForAddIncome);
        dest.writeString(this.sAgentUsername);
        dest.writeString(this.sUserType);
        dest.writeString(this.sKey);
        dest.writeString(this.sMaxDate);
        dest.writeString(this.sMaxSeats);
        dest.writeString(this.sName);
        dest.writeString(this.sLoginHistory);
        dest.writeString(this.sUserId);
    }

    protected Agent(Parcel in) {
        this.Status = in.readInt();
        this.sMessage = in.readString();
        this.sAgentId = in.readString();
        this.sAgentName = in.readString();
        this.sAgentCurrency = in.readString();
        this.sIsGrantedForTicketSelling = in.readString();
        this.sIsGrantedForStandingTicket = in.readString();
        this.sIsGrantedForCargoTicket = in.readString();
        this.sIsGrantedForStatements = in.readString();
        this.sIsGrantedForAgentSummary = in.readString();
        this.sIsGrantedForAssignConductorBus = in.readString();
        this.sIsGrantedForAssignDriverBus = in.readString();
        this.sIsGrantedForMechanicBus = in.readString();
        this.sIsGrantedForActivateAgent = in.readString();
        this.sIsGrantedForPrintTicket = in.readString();
        this.sIsGrantedForPassengerList = in.readString();
        this.sIsGrantedForCargoList = in.readString();
        this.sAgentFloatBal = in.readString();
        this.sIsGrantedToShowFloat = in.readString();
        this.sIsGrantedToStockPrint = in.readString();
        this.sIsGrantedToStockOut = in.readString();
        this.sIsGrantedToStockIn = in.readString();
        this.sIsGrantedForAddExpense = in.readString();
        this.sIsGrantedForAddIncome = in.readString();
        this.sAgentUsername = in.readString();
        this.sUserType = in.readString();
        this.sKey = in.readString();
        this.itemArrayList = in.readParcelable(JsonObject.class.getClassLoader());
        this.sMaxDate = in.readString();
        this.sMaxSeats = in.readString();
        this.sName = in.readString();
        this.sLoginHistory = in.readString();
        this.sUserId = in.readString();
    }

    public static final Creator<Agent> CREATOR = new Creator<Agent>() {
        @Override
        public Agent createFromParcel(Parcel source) {
            return new Agent(source);
        }

        @Override
        public Agent[] newArray(int size) {
            return new Agent[size];
        }
    };
}