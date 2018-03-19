package ca.mcgill.cs.util.concurrent.util;

public class QOpRecord implements Comparable<QOpRecord> {
    public QOp operation;
    public long time_stamp;
    public int id;

    public QOpRecord(QOp pOperation, long pTime_stamp, int pId) {
        operation = pOperation;
        time_stamp = pTime_stamp;
        id = pId;
    }

    @Override
    public int compareTo(QOpRecord o) {
        if (this.time_stamp < o.time_stamp) {
            return -1;
        } else if (this.time_stamp > o.time_stamp) {
            return 1;
        } else {
            if (this.operation == QOp.ENQ && o.operation == QOp.DEQ) {
                return -1;
            } else if (this.operation == QOp.DEQ && o.operation == QOp.ENQ) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
