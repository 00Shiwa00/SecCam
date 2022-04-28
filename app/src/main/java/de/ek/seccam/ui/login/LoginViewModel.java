package de.ek.seccam.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import de.ek.seccam.data.LoginRepository;
import de.ek.seccam.data.Result;
import de.ek.seccam.data.model.LoggedInUser;
import de.ek.seccam.R;

public class LoginViewModel extends ViewModel {


    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;
    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, Context context) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(new LoggedInUser(data.getUserId(), data.getDisplayName(), data.getpassword()))));
        }
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(new LoggedInUser(data.getUserId(), data.getDisplayName(), data.getpassword()))));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }
    public void register(String username, String password, Context context)
    {
        Result<LoggedInUser> result =  loginRepository.register(username, password);
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(new LoggedInUser(data.getUserId(), data.getDisplayName(), data.getpassword()))));
        } else {
            if (result instanceof Result.Error) {
                Result.Error error = (Result.Error) result;
                loginResult.setValue(new LoginResult(R.string.register_failed));
                Toast.makeText(context, "User already exists!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                loginResult.setValue(new LoginResult(R.string.register_failed));
            }

        }

    }


    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() >= 10;
    }
}