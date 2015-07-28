import com.wswenyue.SocketClient.ARMClient;
import com.wswenyue.constant.Constant;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {

        IoAcceptor acceptor = null;
        try {
            //创建一个非阻塞的server端的Socket
            acceptor = new NioSocketAcceptor();
            //设置过滤器（使用Mina提供的文本换行符编解码器）
            acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
            // 设置读取数据的缓冲区大小
            acceptor.getSessionConfig().setReadBufferSize(2048);
            // 读写通道10秒内无操作进入空闲状态
//            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 5);
            // 绑定逻辑处理器
            acceptor.setHandler(new MyServerHandler());
            //绑定端口
            acceptor.bind(new InetSocketAddress(Constant.SERVER_PORT));
            System.out.println("服务端启动成功...端口号为：" + Constant.SERVER_PORT );
            //TODO ARM
            ARMClient armClient = new ARMClient();

        } catch (IOException e) {
            System.out.println("服务器启动异常..." + e);
            e.printStackTrace();
        }
    }
}
