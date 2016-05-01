package com.mtbs3d.minecrift.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import com.google.common.base.CaseFormat;
import com.mtbs3d.minecrift.gui.framework.BaseGuiSettings;
import com.mtbs3d.minecrift.gui.framework.GuiButtonEx;
import com.mtbs3d.minecrift.gui.framework.GuiEventEx;
import com.mtbs3d.minecrift.gui.framework.GuiSliderEx;
import com.mtbs3d.minecrift.gui.framework.GuiSmallButtonEx;
import com.mtbs3d.minecrift.settings.VRSettings;
import com.mtbs3d.minecrift.settings.VRSettings.VrOptions;

public class GuiViveControllerSettings extends BaseGuiSettings implements GuiEventEx {

	static VRSettings.VrOptions[] viveControllerSettings = new VRSettings.VrOptions[]
	{
		VRSettings.VrOptions.TRIGGER_SENSITIVITY
	};
	
	public GuiViveControllerSettings(GuiScreen guiScreen, VRSettings guiVRSettings) {
		super(guiScreen, guiVRSettings);
		screenTitle = "Vive Controller Settings";
	}
	
	/**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
    	this.buttonList.clear();
    	this.buttonList.add(new GuiButtonEx(ID_GENERIC_DEFAULTS, this.width / 2 - 100, this.height / 6 + 148, "Reset To Defaults"));
        this.buttonList.add(new GuiButtonEx(ID_GENERIC_DONE, this.width / 2 - 100, this.height / 6 + 168, "Done"));
        VRSettings.VrOptions[] buttons = viveControllerSettings;
        
        for (int i = 2; i < buttons.length + 2; ++i) {
        	VRSettings.VrOptions var8 = buttons[i - 2];
            int width = this.width / 2 - 155 + i % 2 * 160;
            int height = this.height / 6 + 21 * (i / 2) - 10;

            if (var8.getEnumFloat())
            {
                float minValue = 0.0f;
                float maxValue = 1.0f;
                float increment = 0.01f;

                if (var8 == VRSettings.VrOptions.TRIGGER_SENSITIVITY)
                {
                    minValue = 0.06f;
                    maxValue = 1.0f;
                    increment = 0.01f;
                }

                GuiSliderEx slider = new GuiSliderEx(var8.returnEnumOrdinal(), width, height - 20, var8, this.guivrSettings.getKeyBinding(var8), minValue, maxValue, increment, this.guivrSettings.getOptionFloatValue(var8));
                slider.setEventHandler(this);
                slider.enabled = getEnabledState(var8);
                this.buttonList.add(slider);
            }
            else
            {
                GuiSmallButtonEx smallButton = new GuiSmallButtonEx(var8.returnEnumOrdinal(), width, height - 20, var8, this.guivrSettings.getKeyBinding(var8));
                smallButton.setEventHandler(this);
                smallButton.enabled = getEnabledState(var8);
                this.buttonList.add(smallButton);
            }
        }
    }
    
    private boolean getEnabledState(VRSettings.VrOptions e)
    {
    	return true;
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton guiButton)
    {
    	VRSettings vr = mc.vrSettings;
    	System.out.println(vr.triggerSensitivity);
    	
    	if (guiButton.enabled) {
    		if (guiButton.id == ID_GENERIC_DONE) {
    			Minecraft.getMinecraft().vrSettings.saveOptions();
    			this.mc.displayGuiScreen(this.parentGuiScreen);
    		} else if (guiButton.id == ID_GENERIC_DEFAULTS) {
    			//Set defaults
    			this.guivrSettings.triggerSensitivity = 1.0f;
    			
    			this.guivrSettings.saveOptions();
    			this.reinit = true;
    		}else if (guiButton instanceof GuiSmallButtonEx) {
                VRSettings.VrOptions num = VRSettings.VrOptions.getEnumOptions(guiButton.id);
                this.guivrSettings.setOptionValue(((GuiSmallButtonEx)guiButton).returnVrEnumOptions(), 1);
                guiButton.displayString = this.guivrSettings.getKeyBinding(VRSettings.VrOptions.getEnumOptions(guiButton.id));
            }
    	}
    }
    
    @Override
    protected String[] getTooltipLines(String displayString, int buttonId)
    {
    	VRSettings.VrOptions e = VRSettings.VrOptions.getEnumOptions(buttonId);
        if( e != null )
            switch(e)
            {
            	case TRIGGER_SENSITIVITY:
            		return new String[] {
            			"Sets the point when the controller clicks on trigger pull.",
            			"0.06: Hair trigger",
            			"1.0: Requires trigger to be fully depressed and click"
            		};
            	default:
            		return null;
            }
        else
        	return null;
    }
    
	@Override
	public boolean event(int id, VrOptions enumm) {
		if (enumm == VRSettings.VrOptions.USE_PROFILE_PLAYER_HEIGHT)
        {
            this.reinit = true;
        }

        return true;
	}

	@Override
	public boolean event(int id, String s) {
		return true;
	}
}
