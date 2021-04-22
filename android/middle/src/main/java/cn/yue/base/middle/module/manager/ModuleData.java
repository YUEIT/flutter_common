package cn.yue.base.middle.module.manager;

public class ModuleData<T> {

    private int priority;
    private Class interfaceClass;
    private Class impClass;
    private T service;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Class getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public Class<T> getImpClass() {
        return impClass;
    }

    public void setImpClass(Class impClass) {
        this.impClass = impClass;
    }

    public T getService() {
        return service;
    }

    public void setService(T service) {
        this.service = service;
    }
}
