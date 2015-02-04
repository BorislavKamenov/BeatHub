package AdaptersAndAbstractClasses;

/**
 * Created by Borislav on 21.1.2015 г..
 */
public class Utils {

    public static String durationFormat(long duration){

        long res = duration / 1000;

        int hours = (int) res / 3600;
        int minutes = (int) (res / 60) % 60;
        int seconds = (int) res % 60;

        if(hours == 0){
            return String.format("%02d:%02d", minutes, seconds);
        }
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /*
    current duration in percent
     */
    public static int getProgressPercentage(long currentDuration, long totalDuration){
        double percentage =  0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentage
        return (int) percentage;
    }

    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }
}
