package seg2105.uottawa.com.taskmanager;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

public class AddEquipment extends DialogFragment {
    EditText txtEquipment;
    Button btnCancel;
    static String DialogboxTitle;

    public interface InputNameDialogListener {
        void onFinishInputDialog(String inputText);
    }

    //---empty constructor required
    public AddEquipment() {

    }
    //---set the title of the dialog window
    public void setDialogTitle(String title) {
        DialogboxTitle = title;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){

        View view = inflater.inflate(
                R.layout.nav_equipment, container);

        //---get the EditText and Button views
        txtEquipment = (EditText) view.findViewById(R.id.txtEquipment);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        //---event handler for the button
        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                dismiss();
            }
        });

        //---show the keyboard automatically
        txtEquipment.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //---set the title for the dialog
        getDialog().setTitle(DialogboxTitle);

        return view;
    }
}