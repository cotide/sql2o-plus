package io.github.cotide.core.provider.Internal;

public class Tuple<T1,T2,T3,T4> {

    private final T1 m_Item1;
    private final T2 m_Item2;
    private final T3 m_Item3;
    private final T4 m_Item4;

    public T1 getItem1(){
        return m_Item1;
    }

    public T2 getItem2(){
        return m_Item2;
    }

    public T3 getItem3(){
        return m_Item3;
    }

    public T4 getItem4(){
        return m_Item4;
    }

    public Tuple(T1 item1, T2 item2, T3 item3, T4 item4){
        m_Item1 = item1;
        m_Item2 = item2;
        m_Item3 = item3;
        m_Item4 = item4;
    }
}
