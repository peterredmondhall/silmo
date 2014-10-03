package com.gwt.wizard.client.steps;

import static com.gwt.wizard.client.GwtWizard.MESSAGES;
import static com.gwt.wizard.client.util.EmailValidator.isEmailValid;

import com.google.gwt.user.client.ui.Composite;
import com.gwt.wizard.client.core.WizardStep;
import com.gwt.wizard.client.steps.ui.ContactStepUi;
import com.gwt.wizard.shared.model.BookingInfo;

public class ContactStep implements WizardStep
{

    private final ContactStepUi ui;
    private final BookingInfo bookingInfo;

    public ContactStep(BookingInfo bookingInfo)
    {
        ui = new ContactStepUi();
        this.bookingInfo = bookingInfo;
    }

    @Override
    public String getCaption()
    {
        return MESSAGES.secondPage();
    }

    @Override
    public Composite getContent()
    {
        return ui;
    }

    @Override
    public Boolean onNext()
    {
        ui.setOrganizerNameErrorMsg("");
        if (ui.getOrganizerName() == null || ui.getOrganizerName().trim().length() == 0)
        {
            ui.setOrganizerNameErrorMsg(MESSAGES.organizerNameErrorMsg());
            return false;
        }
        ui.setOrganizerEmailErrorMsg("");
        if (!isEmailValid(ui.getOrganizerEmail()))
        {
            ui.setOrganizerEmailErrorMsg(MESSAGES.organizerEmailErrorMsg());
            return false;
        }

        ui.setCompanionNameErrorMsg("");
        if (ui.getCompanionName() == null || ui.getCompanionName().trim().length() == 0)
        {
            ui.setCompanionNameErrorMsg(MESSAGES.companionNameErrorMsg());
            return false;
        }
        ui.setCompanionEmailErrorMsg("");
        if (!isEmailValid(ui.getCompanionEmail()))
        {
            ui.setCompanionEmailErrorMsg(MESSAGES.companionEmailErrorMsg());
            return false;
        }

        bookingInfo.setOrganizerName(ui.getOrganizerName());
        bookingInfo.setOrganizerEmail(ui.getOrganizerEmail());

        bookingInfo.setCompanionName(ui.getCompanionName());
        bookingInfo.setCompanionEmail(ui.getCompanionEmail());

        return true;
    }

    @Override
    public Boolean onBack()
    {
        return true;
    }

    @Override
    public void clear()
    {

    }
}
