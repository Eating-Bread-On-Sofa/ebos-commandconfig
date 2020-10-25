package cn.edu.bjtu.eboscommandconfig.service;

import cn.edu.bjtu.eboscommandconfig.entity.Gateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GatewayService {
    public Gateway findGatewayByName(String name);
    public Gateway findGatewayByIp(String ip);
    public List<Gateway> findAllGateway();
}
