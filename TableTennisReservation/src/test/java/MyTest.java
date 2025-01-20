import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.enums.ReserveStatus;
import com.atsu.tabletennisreservation.pojo.ReserveDate;

import java.util.List;

public class MyTest {
    public static void main(String[] args) {
        ReserveDate reserveDate=new ReserveDate();
        boolean b = reserveDate.reserveProcess(12,2);
        reserveDate.reserveProcess(12,5);
        System.out.println(reserveDate.getDateList());
        System.out.println(b);
    }
}
