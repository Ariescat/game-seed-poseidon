#### 注册过程

- `GroovyFactory`

  注册`BeanDefinition`，注意看这里：
  
  ```java
  bd.setBeanClassName("org.springframework.scripting.groovy.GroovyScriptFactory");
  ```
  
  也就是最终产生能执行的class是在这个工厂里编译出来的


- `org.springframework.scripting.support.ScriptFactoryPostProcessor`，看`spring`对`groovy`的支持

	- 先执行`setBeanFactory`，这里有一行关键的代码：

	  ```java
	  // Required so that all BeanPostProcessors, Scopes, etc become available.
    this.scriptBeanFactory.copyConfigurationFrom(this.beanFactory);
	  ```
  
    **附：**
  
    因为这里拷贝了spring默认的工厂，此后的groovy的构造是`scriptBeanFactory`，所以如果想对groovy的代码进行扩展，则必要要在`ScriptFactoryPostProcessor`执行之前做处理。
  
    * `RpcConsumerProcessor`
  
      如新增一个`RpcConsumerProcessor`对groovy类注解有`@RpcConsumer`的字段进行RPC代理注入，则必要提高`RpcConsumerProcessor`的优先级，可以考虑实现`PriorityOrdered`，不然的话`scriptBeanFactory` `copy`完了是拿不到`RpcConsumerProcessor`的，更别说执行了）
  
      至于为什么会这样就要看Spring的代码了：`AbstractApplicationContext#registerBeanPostProcessors`：
  
      ```java
      // First, register the BeanPostProcessors that implement PriorityOrdered.
      OrderComparator.sort(priorityOrderedPostProcessors);
      registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors); // 所以必须要在下面ScriptFactoryPostProcessor构造之前把自定义的Processors优先注册进去
      
      // Next, register the BeanPostProcessors that implement Ordered.
      List<BeanPostProcessor> orderedPostProcessors = new ArrayList<BeanPostProcessor>();
      for (String ppName : orderedPostProcessorNames) {
      	BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class); // 这里就会构造ScriptFactoryPostProcessor并调用setBeanFactory
      	orderedPostProcessors.add(pp);
      	if (pp instanceof MergedBeanDefinitionPostProcessor) {
      		internalPostProcessors.add(pp);
      	}
      }
      ```
  
    * `RpcProviderProcessor`
  
      这个`Processor`又为什么不需要`PriorityOrdered`更改优先级呢，因为他不需要获取**实例**来注入，只需要获取到`targetClass`就行了，而这个Spring的`AopUtils.getTargetClass(bean)`就可以完美的获取到代理的Class，从而获取到该Class的字段和方法。
  
      
  
  - 再看`predictBeanType`和`postProcessBeforeInstantiation`
  
    这两个方法都是调用到`prepareScriptBeans`方法，点进去，再看`createScriptedObjectBeanDefinition`：
  
    ```java
    GenericBeanDefinition objectBd = new GenericBeanDefinition(bd); // 传进父bd，也就是一开始我们GroovyFactory构建的bd
    objectBd.setFactoryBeanName(scriptFactoryBeanName);
    objectBd.setFactoryMethodName("getScriptedObject"); // 看到这里是不是就和GroovyScriptFactor对应上啦
    objectBd.getConstructorArgumentValues().clear();
    objectBd.getConstructorArgumentValues().addIndexedArgumentValue(0, scriptSource);
    objectBd.getConstructorArgumentValues().addIndexedArgumentValue(1, interfaces);
    ```
  
    `createScriptedObjectBeanDefinition`调用完后，会执行：
  
    ```java
    if (refreshCheckDelay >= 0) {
    	objectBd.setScope(BeanDefinition.SCOPE_PROTOTYPE); // 这一步很重要，此后的脚本热替换要用到
    }
    ```
  
    **附：**
  
    其实这里的`scriptFactory`和`scriptSource`没太看懂是干嘛的，以后研究下
  
    ```java
    ScriptFactory scriptFactory = this.scriptBeanFactory.getBean(scriptFactoryBeanName, ScriptFactory.class);
    ScriptSource scriptSource = getScriptSource(scriptFactoryBeanName, scriptFactory.getScriptSourceLocator());
    ```
  
    
  
  - 执行到`postProcessBeforeInstantiation`，才真正调用`createRefreshableProxy`：
  
    ```java
  RefreshableScriptTargetSource ts = new RefreshableScriptTargetSource(this.scriptBeanFactory,
    		scriptedObjectBeanName, scriptFactory, scriptSource, isFactoryBean);
  ...
    return createRefreshableProxy(ts, interfaces, proxyTargetClass);
    ```
    
    看`createRefreshableProxy`这个方法内部，事实上是调用了`new JdkDynamicAopProxy(config)`创建了一个代理对象。此后调用接口的方法则会这样执行:
    
    ```java
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ...
        TargetSource targetSource = this.advised.targetSource; // 这里的targetSource就是RefreshableScriptTargetSource
        ...
        target = targetSource.getTarget(); // 看父类的getTarget
        ...
        // 接下来就是反射调用了
    }
    ```
    
    **附：**
    
    其实这里还有一个坑：`groovy`注册进`spring`，但没地方引用，启动的时候是不会进`postProcessBeforeInstantiation`这个方法的。虽然以后调用`getBean`会重新进来，但如果是想在启动的时候做一些工作，则要注意这一点。
    
    
    
  - `getTarget`是怎么拿到实例化的`groovy`对象呢？