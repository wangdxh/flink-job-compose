
package com.kedacom.flinketlgraph.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Graph implements Serializable
{

    /**
     * graphconfig配置
     * (Required)
     * 
     */
    private Graphconfig graphconfig;
    /**
     * 
     * (Required)
     * 
     */
    private List<Graphnode> nodes = new ArrayList<Graphnode>();
    /**
     * 
     * (Required)
     * 
     */
    private List<Graphlink> links = new ArrayList<Graphlink>();
    private final static long serialVersionUID = 330105535226L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Graph() {
    }

    /**
     * 
     * @param graphconfig
     * @param nodes
     * @param links
     */
    public Graph(Graphconfig graphconfig, List<Graphnode> nodes, List<Graphlink> links) {
        super();
        this.graphconfig = graphconfig;
        this.nodes = nodes;
        this.links = links;
    }

    /**
     * graphconfig配置
     * (Required)
     * 
     */
    public Graphconfig getGraphconfig() {
        return graphconfig;
    }

    /**
     * graphconfig配置
     * (Required)
     * 
     */
    public void setGraphconfig(Graphconfig graphconfig) {
        this.graphconfig = graphconfig;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<Graphnode> getNodes() {
        return nodes;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setNodes(List<Graphnode> nodes) {
        this.nodes = nodes;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<Graphlink> getLinks() {
        return links;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setLinks(List<Graphlink> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Graph.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("graphconfig");
        sb.append('=');
        sb.append(((this.graphconfig == null)?"<null>":this.graphconfig));
        sb.append(',');
        sb.append("nodes");
        sb.append('=');
        sb.append(((this.nodes == null)?"<null>":this.nodes));
        sb.append(',');
        sb.append("links");
        sb.append('=');
        sb.append(((this.links == null)?"<null>":this.links));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.graphconfig == null)? 0 :this.graphconfig.hashCode()));
        result = ((result* 31)+((this.links == null)? 0 :this.links.hashCode()));
        result = ((result* 31)+((this.nodes == null)? 0 :this.nodes.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Graph) == false) {
            return false;
        }
        Graph rhs = ((Graph) other);
        return ((((this.graphconfig == rhs.graphconfig)||((this.graphconfig!= null)&&this.graphconfig.equals(rhs.graphconfig)))&&((this.links == rhs.links)||((this.links!= null)&&this.links.equals(rhs.links))))&&((this.nodes == rhs.nodes)||((this.nodes!= null)&&this.nodes.equals(rhs.nodes))));
    }

}
