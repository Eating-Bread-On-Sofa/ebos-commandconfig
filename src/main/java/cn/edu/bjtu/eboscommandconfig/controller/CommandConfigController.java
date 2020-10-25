package cn.edu.bjtu.eboscommandconfig.controller;

import cn.edu.bjtu.eboscommandconfig.entity.Command;
import cn.edu.bjtu.eboscommandconfig.entity.Gateway;
import cn.edu.bjtu.eboscommandconfig.entity.ListedCommand;
import cn.edu.bjtu.eboscommandconfig.service.GatewayService;
import cn.edu.bjtu.eboscommandconfig.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

@Api(tags = "指令管理")
@RequestMapping("/api/commandconfig")
@RestController
public class CommandConfigController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LogService logService;
    @Autowired
    GatewayService gatewayService;

    @ApiOperation(value = "查看指定网关设备所支持的指令",notes = "用于创建指令期间要选择设备及相关资源时，填充下拉菜单")
    @CrossOrigin
    @GetMapping("/list/{name}")
    public List<ListedCommand> checkCommandOne(@PathVariable String name){
        Gateway gateway = gatewayService.findGatewayByName(name);
        System.out.println(gateway);
        String url = "http://"+ gateway.getIp() +":8082/api/command/list";
        List<ListedCommand> listedCommands = new LinkedList<>();
        try{
            listedCommands = restTemplate.getForObject(url,List.class);
        }catch (Exception e) {
            logService.error("retrieve","无法连接至网关"+gateway.getName()+":"+gateway.getIp()+" 异常:"+e.toString());
            listedCommands = new LinkedList<>();
        }
        return listedCommands;
    }

    @ApiOperation(value = "向指定网关添加指令",notes = "所属网关为部署了本微服务的网关")
    @CrossOrigin
    @PostMapping("/{name}")
    public String add(@RequestBody Command info,@PathVariable String name){
        Gateway gateway = gatewayService.findGatewayByName(name);
        String url = "http://"+ gateway.getIp() +":8082/api/command";
        String response = "";
        try{
            response = restTemplate.postForObject(url,info,String.class);
        }catch (Exception e) {
            logService.error("retrieve","无法连接至网关"+gateway.getName()+":"+gateway.getIp()+" 异常:"+e.toString());
            response = "";
        }
        return response;
    }


    @ApiOperation(value = "向指定网关恢复指令",notes = "与添加不同的是，其一可以批量恢复，其二添加是不允许重名，恢复时如果重名会选择覆盖策略")
    @CrossOrigin
    @PostMapping("/recover/{name}")
    public String plus(@RequestBody Command[] commands,@PathVariable String name){
        Gateway gateway = gatewayService.findGatewayByName(name);
        String url = "http://"+ gateway.getIp() +":8082/api/command/recover";
        String response = "";
        try {
            response = restTemplate.postForObject(url,commands,String.class);
        }catch (Exception e) {
            logService.error("retrieve", "无法连接至网关" + gateway.getName() + ":" + gateway.getIp() + " 异常:" + e.toString());
            response = "";
        }
        return response;
    }

    @ApiOperation(value = "向指定网关删除指令")
    @CrossOrigin
    @DeleteMapping("/{name}")
    public boolean delete(@RequestParam String commandname,@PathVariable String name){
        Gateway gateway = gatewayService.findGatewayByName(name);
        String url = "http://"+ gateway.getIp() +":8082/api/command";
        boolean response = false;
        try {
            restTemplate.delete(url,commandname,boolean.class);
            response = true;
        }catch (Exception e) {
            logService.error("retrieve", "无法连接至网关" + gateway.getName() + ":" + gateway.getIp() + " 异常:" + e.toString());
            response = false;
        }
        return response;
    }

    @ApiOperation(value = "查看指定网关设备所有指令的详细信息", notes = "用于显示已有指令列表")
    @CrossOrigin
    @GetMapping("/{name}")
    public List<Command> show(@PathVariable String name){
        Gateway gateway = gatewayService.findGatewayByName(name);
        String url = "http://"+ gateway.getIp() +":8082/api/command";
        List<Command> commands = new LinkedList<>();
        try {
            commands = restTemplate.getForObject(url,List.class);
        }catch (Exception e) {
            logService.error("retrieve", "无法连接至网关" + gateway.getName() + ":" + gateway.getIp() + " 异常:" + e.toString());
            commands = new LinkedList<>();
        }
        return commands;
    }


    @ApiOperation(value = "微服务运行检测", notes = "微服务正常运行时返回 pong")
    @CrossOrigin
    @GetMapping("/ping")
    public String ping(){
        logService.info("retrieve","对指令管理进行了一次健康检测");
        return "pong";
    }
}
