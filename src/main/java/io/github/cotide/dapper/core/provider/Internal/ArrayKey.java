package io.github.cotide.core.provider.Internal;

public class ArrayKey<T> {

    private int _hashCode;

    private T[] _keys;

    public ArrayKey(T[] keys)
    {
        _keys = keys;

        _hashCode = 17;
        for (T k:
             keys) {
            _hashCode = _hashCode*23 + (k == null ? 0 : k.hashCode());
        }
    }

    private Boolean equals(ArrayKey<T> other)
    {
        if (other == null)
            return false;

        if (other._hashCode != _hashCode)
            return false;

        if (other._keys.length != _keys.length)
            return false;

        for (int i = 0; i < _keys.length; i++)
        {
            if(!(_keys[i].equals(other._keys[i])))
                return false;
        }
        return true;
    }

    public int GetHashCode()
    {
        return _hashCode;
    }
}
