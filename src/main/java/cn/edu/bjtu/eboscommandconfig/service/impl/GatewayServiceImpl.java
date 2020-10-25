package cn.edu.bjtu.eboscommandconfig.service.impl;

import cn.edu.bjtu.eboscommandconfig.entity.Gateway;
import cn.edu.bjtu.eboscommandconfig.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GatewayServiceImpl implements GatewayService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Gateway findGatewayByName(String name){
        Query query = Query.query(Criteria.where("name").is(name));
        Gateway gateway = mongoTemplate.findOne(query,Gateway.class,"Gateway");
        return gateway;
    }

    @Override
    public Gateway findGatewayByIp(String ip){
        Query query = Query.query(Criteria.where("ip").is(ip));
        Gateway gateway = mongoTemplate.findOne(query,Gateway.class,"Gateway");
        return gateway;
    }

    @Override
    public List<Gateway> findAllGateway(){
        return mongoTemplate.findAll(Gateway.class,"Gateway");
    }
}
