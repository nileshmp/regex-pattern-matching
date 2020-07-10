package nilesh.regex.utils;

import java.util.Objects;

public class IndexPair
{
    private int count;
    private int innerCount;

    public IndexPair (int count, int innerCount)
    {
        this.count = count;
        this.innerCount = innerCount;
    }

    public int getCount ()
    {
        return count;
    }

    public int getInnerCount ()
    {
        return innerCount;
    }

    @Override
    public boolean equals (Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        IndexPair indexPair = (IndexPair)o;
        return ((count == indexPair.count &&
            innerCount == indexPair.innerCount) || (count == indexPair.innerCount &&
            innerCount == indexPair.count));
    }

    @Override
    public int hashCode ()
    {
        int countHash = Objects.hash(count);
        int innerCountHash = Objects.hash(innerCount);
        int hash = (count <= innerCount) ?
            Objects.hash(countHash, innerCountHash) :
            Objects.hash(innerCountHash, countHash);
        return hash;
    }
}
