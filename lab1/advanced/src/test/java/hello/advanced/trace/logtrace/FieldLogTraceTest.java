package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldLogTraceTest {

    FieldLogTrace trace = new FieldLogTrace();

    @Test
    void begin_end() {
        TraceStatus status1 = trace.begin("test1");
        TraceStatus status2 = trace.begin("test2");
        TraceStatus status3 = trace.begin("test3");

        trace.end(status1);
        trace.end(status2);
        trace.end(status3);
    }

    @Test
    void begin_ex() {
        TraceStatus status1 = trace.begin("test1");
        TraceStatus status2 = trace.begin("test2");
        TraceStatus status3 = trace.begin("test3");

        trace.exception(status3, new IllegalStateException("ex"));
        trace.end(status2);
        trace.end(status1);
    }
}