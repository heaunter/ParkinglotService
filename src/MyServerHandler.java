import com.wswenyue.constant.Constant;
import com.wswenyue.protocol.MsgAnalysis;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * Created by wswenyue on 2015/5/23.
 */
public class MyServerHandler extends IoHandlerAdapter {



    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("sessionCreated");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("sessionOpened");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("sessionClosed");
        session.close(true);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("sessionIdle");
        //TODO
//                Thread t = new Thread(new ARMClient("e"+
//                        RandomNumber.getInstance().makeNumber(2)
//                        +"#"+RandomNumber.getInstance().makeNumber(2)+"#"));
//                t.start();
    }

    /**
     * 接受消息
     * */
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String msg = message.toString();
//        System.out.println("接收到消息：" + msg);
//        session.write(msg);

        //异常处理
        if(msg==null || msg.trim().equals("")){
            session.write(Constant.Server_Unknown);
        }
        if("bye".equalsIgnoreCase(msg)){
            session.close(true);
        }

        String result = MsgAnalysis.Analysis(msg);
        if(!result.equals("")){
            System.out.println("发送消息到phone：" + result);
            session.write(result);
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        System.out.println("messageSent");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        System.out.println("连接异常");
//        session.close(true);
    }
}
