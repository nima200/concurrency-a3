package ca.mcgill.cs.comp409.a3.q1.concurrent.util;

public class QOpRecord implements Comparable {
    public QOp operation;
    public long time_stamp;
    public int id;

    public QOpRecord(QOp pOperation, long pTime_stamp, int pId) {
        operation = pOperation;
        time_stamp = pTime_stamp;
        id = pId;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof QOpRecord)) {
            throw new ClassCastException("A QOpRecord object expected");
        }
        QOpRecord other = (QOpRecord) o;
        if (this.time_stamp < other.time_stamp) {
            return -1;
        } else if (this.time_stamp > other.time_stamp) {
            return 1;
        } else {
            if (this.operation == QOp.enq && other.operation == QOp.deq) {
                return -1;
            } else if (this.operation == QOp.deq && other.operation == QOp.enq) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
