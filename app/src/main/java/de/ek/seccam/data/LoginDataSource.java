package de.ek.seccam.data;

import android.content.Context;
import android.util.Log;

import de.ek.seccam.AES;
import de.ek.seccam.data.model.LoggedInUser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;

import javax.crypto.ExemptionMechanismException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    Context context;
    public LoginDataSource(Context context){
        this.context=context;
    }
    public Result<LoggedInUser> login(String username, String password) {

        try{
            File authfile = null;
            File directory = new File(context.getFilesDir().getAbsolutePath()+"/userdata/");
            File[] files = directory.listFiles();
            Log.d("Files", "Size: "+ files.length);
            for (int i = 0; i < files.length; i++)
            {
                Log.d("Files", "FileName:" + files[i].getName());
                if(files[i].getName().replace(".txt","").equals(username)) {
                    authfile = files[i];
                    break;
                }
            }
            if(authfile == null)
                return new Result.Error(new FileNotFoundException("User not found"));

            int size = (int) authfile.length();
            byte[] bytes = new byte[size];
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(authfile));
            buf.read(bytes, 0, bytes.length);
            buf.close();
            String s = new String(new AES().decrypt(bytes, password), StandardCharsets.UTF_8);
            if(new String(new AES().decrypt(bytes, password), StandardCharsets.UTF_8).equals("authentification successful"))
            {
                LoggedInUser User =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                "Jane Doe",password);
                return new Result.Success<>(User);
            }
            return new Result.Error(new ExemptionMechanismException("Password wrong!"));
        } catch(Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }


    public Result<LoggedInUser> register(String username, String password){
        try {
            File directory = new File(context.getFilesDir().getAbsolutePath()+"/userdata/");
            if(!directory.exists()) {
                directory.mkdirs();
                directory = new File(context.getFilesDir().getAbsolutePath()+"/userdata/");
            }
            File[] files = directory.listFiles();
            Log.d("Files", "Size: "+ files.length);
            for (int i = 0; i < files.length; i++)
            {
                Log.d("Files", "FileName:" + files[i].getName());
                if(files[i].getName().replace(".txt","").equals(username))
                    return new Result.Error(new FileAlreadyExistsException(""));
            }
            /*
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");
            kpg.initialize(new KeyGenParameterSpec.Builder(
                    username,
                    KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                    .setDigests(KeyProperties.DIGEST_SHA256,
                            KeyProperties.DIGEST_SHA512)
                    .build());

            KeyPair kp = kpg.generateKeyPair();
            AES aes = new AES();
            // store symmetric key
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create("usernamekey",context);
            byte[] encyptedpassword = aes.encrypt(password.getBytes(StandardCharsets.UTF_8),Base64.encodeToString(kp.getPrivate().getEncoded(),Base64.DEFAULT));
            byte[] encodedSymmetricKey = kp.getPrivate().getEncoded();
            SharedPreferences.Editor edit = sharedPreferences.edit();
            String base64EncodedSymmetricKey = new String(Base64.getEncoder().encode(encodedSymmetricKey));
            edit.putString(alias, base64EncodedSymmetricKey);
            edit.commit();*/
            //create File for password verification...could be used to crack password...
            File authfile = new File(context.getFilesDir().getAbsolutePath()+"/userdata/"+username+".txt");
            FileOutputStream fout = new FileOutputStream(authfile);
            fout.write(new AES().encrypt("authentification successful".getBytes(StandardCharsets.UTF_8),password));
            fout.flush();
            fout.close();
            //TODO store password save!!!
            return new Result.Success<>(new LoggedInUser(java.util.UUID.randomUUID().toString(),
                    "Jane Doe",password));
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }
}