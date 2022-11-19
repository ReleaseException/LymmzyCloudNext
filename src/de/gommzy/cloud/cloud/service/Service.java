package de.gommzy.cloud.cloud.service;

import de.gommzy.cloud.LymmzyCloud;
import de.gommzy.cloud.cloud.cloud.Node;
import de.gommzy.cloud.cloud.templates.configuration.TemplateConfiguration;

import java.util.List;

public class Service {

    private final String serviceName;
    private final Process process;
    private final TemplateConfiguration configuration;
    private final Node node;

    public Service(String serviceName, Process process, TemplateConfiguration configuration, Node node) {
        //TODO params servicename e.g. Lobby-1, process, TemplateConfiguration, Node
        this.serviceName = serviceName;
        this.process = process;
        this.configuration = configuration;
        this.node = node;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Process getProcess() {
        return process;
    }

    public TemplateConfiguration getConfiguration() {
        return configuration;
    }

    public Node getNode() {
        return this.node;
    }

    public void registerService() {
        List<Service> serviceList = LymmzyCloud.services.get(this.getConfiguration());
        serviceList.add(this);
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceName='" + serviceName + '\'' +
                ", process=" + process +
                ", configuration=" + configuration +
                '}';
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
