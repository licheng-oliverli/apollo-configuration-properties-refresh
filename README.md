```xml
<dependency>
    <groupId>com.github.oliver.apollo</groupId>
    <artifactId>apollo-configuration-properties-refresh</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
demo:
```properties
apollo.AutoRefreshConfigurationProperties=true
```
```java
@Component
@ConfigurationProperties(prefix = "redis.cache")
@RefreshScope
public class SampleRedisConfig implements InitializingBean {
  private int expireSeconds;
  private String clusterNodes;
  private Map  someMap = Maps.newLinkedHashMap();
  private List  someList = Lists.newLinkedList();
  public void setExpireSeconds(int expireSeconds) {
    this.expireSeconds = expireSeconds;
  }
  public void setClusterNodes(String clusterNodes) {
    this.clusterNodes = clusterNodes;
  }
  public Map  getSomeMap() {
    return someMap;
  }
  public List  getSomeList() {
    return someList;
  }
}
```
or
```java
@Component
@ConfigurationProperties(prefix = "redis.cache")
@ApolloConfigurationPropertiesRefresh
public class SampleRedisConfig implements InitializingBean {
  private int expireSeconds;
  private String clusterNodes;
  private Map  someMap = Maps.newLinkedHashMap();
  private List  someList = Lists.newLinkedList();
  public void setExpireSeconds(int expireSeconds) {
    this.expireSeconds = expireSeconds;
  }
  public void setClusterNodes(String clusterNodes) {
    this.clusterNodes = clusterNodes;
  }
  public Map  getSomeMap() {
    return someMap;
  }
  public List  getSomeList() {
    return someList;
  }
}
```