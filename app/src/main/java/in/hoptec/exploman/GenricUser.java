package in.hoptec.exploman;

import com.google.firebase.auth.FirebaseUser;

import in.hoptec.exploman.database.Ouser;

/**
 * Created by shivesh on 29/6/17.
 */

public class GenricUser {

    public String user_name;
    public String user_fname;
    public String user_password;
    public String uid;
    public String suid;
    public String auth;
    public String user_created;
    public String user_email;
    public String user_image;
    public String user_phone;
    public String social;

    public String extra0;
    public String extra1;
    public String extra2;

    public GenricUser(FirebaseUser user)
    {
        user_name=utl.refineString(""+user.getEmail(),"");
        user_fname=""+user.getDisplayName();
        user_password=""+user.getUid();
        uid=""+user.getUid();
        suid=""+user.getUid();
        auth=""+user.getUid();
        user_email=""+user.getEmail();
        user_phone=""+user.getPhoneNumber();
        try {
            user_image=""+user.getPhotoUrl().toString();
        } catch (Exception e) {
            utl.l("NO USER IMAGE at GenricUser(FireBaseUser)");
        }

    }


    public GenricUser(Ouser user)
    {

        user_name= utl.refineString(""+user.userName,"");
        user_fname=""+user.userFname;
        user_password=""+user.auth;
        uid=""+user.uid;
        suid=""+user.suid;
        auth=""+user.auth;
        user_email=""+user.userEmail;
        user_phone=""+user.extra0;
        try {
            user_image=""+user.userImage;
        } catch (Exception e) {
            utl.l("NO USER IMAGE at GenricUser(FireBaseUser)");
        }


    }


    public GenricUser()
    {

    }


}
