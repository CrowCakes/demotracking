package com.example.demotracking.access;

import com.example.demotracking.access.AccessControl;
import com.example.demotracking.classes.ConnectionManager;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;

/*
 * Exception performing whole class analysis.
 */
public class LoginForm
extends CssLayout {
    private TextField username;
    private TextField password;
    private Label label;
    private Button login;
    private AccessControl accessControl;
    private LoginListener loginListener;
    private ConnectionManager manager = new ConnectionManager();

    public LoginForm(AccessControl accessControl, LoginListener loginListener) {
        this.accessControl = accessControl;
        this.loginListener = loginListener;
        this.prepareLayout();
        this.username.focus();
    }

    private void prepareLayout() {
        this.addStyleName("login-screen");
        FormLayout loginForm = this.buildLoginForm();
        
        VerticalLayout centeringLayout = new VerticalLayout();
        centeringLayout.setMargin(false);
        centeringLayout.setSpacing(false);
        centeringLayout.setStyleName("centering-layout");
        centeringLayout.addComponent((Component)loginForm);
        centeringLayout.setComponentAlignment((Component)loginForm, Alignment.MIDDLE_CENTER);
        
        this.setSizeFull();
        this.addComponent((Component)centeringLayout);
    }

    private FormLayout buildLoginForm() {
        FormLayout loginForm = new FormLayout();
        loginForm.addStyleName("login-form");
        loginForm.setSizeUndefined();
        
        this.label = new Label("Enter your e-mail address:");
        loginForm.addComponent(label);
        
        this.username = new TextField("Username");
        loginForm.addComponent((Component)this.username);
        this.username.setWidth(15.0f, Sizeable.Unit.EM);
        
        this.password = new PasswordField("Password");
        //loginForm.addComponent((Component)this.password);
        this.password.setWidth(15.0f, Sizeable.Unit.EM);
        
        CssLayout buttons = new CssLayout();
        buttons.setStyleName("buttons");
        loginForm.addComponent((Component)buttons);
        
        this.login = new Button("Login");
        buttons.addComponent((Component)this.login);
        
        this.login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        this.login.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        this.login.setDisableOnClick(true);
        login.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    login();
                } finally {
                    login.setEnabled(true);
                }
            }
        });
        return loginForm;
    }

    private void login() {
        String result = this.accessControl.signIn(manager, this.username.getValue(), this.password.getValue());
        if (this.accessControl.isRoleValid(result)) {
            System.out.println(result);
            this.loginListener.loginSuccessful();
        } else {
            this.username.focus();
            Notification.show((String)"Login failed", 
            		(String)"Please check your username and password and try again.", 
            		(Notification.Type)Notification.Type.HUMANIZED_MESSAGE);
            System.out.println(result);
        }
    }
    
    public interface LoginListener extends Serializable {
        void loginSuccessful();
    }

    /*
    static Button access$0(LoginForm loginForm) {
        return loginForm.login;
    }

    static void access$1(LoginForm loginForm) {
        loginForm.login();
    }
    */
}

