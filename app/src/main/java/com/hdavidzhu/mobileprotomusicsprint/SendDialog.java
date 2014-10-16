import javax.naming.Context;

public class FireMissilesDialogFragment extends DialogFragment {
    Context context;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_fire_missiles)
                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        Song currentSong = musicSrv.getCurrentSong();

        //post song to Firebase
        snapFirebase.postSnap(currentSong);

        // Create the AlertDialog object and return it
        return builder.create();
    }
}