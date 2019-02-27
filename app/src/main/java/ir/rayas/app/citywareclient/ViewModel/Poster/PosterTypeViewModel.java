package ir.rayas.app.citywareclient.ViewModel.Poster;

/**
 * Created by Programmer on 10/11/2018.
 */

public class PosterTypeViewModel {
    /// <summary>
    /// کد نوع پوستر
    /// </summary>
    public int Id;
    /// <summary>
    /// نام نوع پوستر
    /// </summary>
    public String Name;
    /// <summary>
    /// همیشه پوستر در بالا نمایش داده شود
    /// در صورتی همیشه بالا است که کاربر در منطقه مورد نظر باشد
    /// با فیلد اولویت تفاوت دارد
    /// </summary>
    public boolean IsTop;
    /// <summary>
    /// اندازه تعداد سطر
    /// </summary>
    public int Rows;
    /// <summary>
    /// اندازه تعداد ستون
    /// </summary>
    public int Cols;
    /// <summary>
    /// اگر اولیت 0 باشد پوستر معمولی است در غیر این صورت به ترتیب عدد اولویت در قسمت پوسترها نمایش داده می شود بدون اینکه مکان کاربر را در نظر بگیرد
    /// </summary>
    public int Priority;
    /// <summary>
    /// توضیحات نوع پوتسر
    /// </summary>
    public String Description;
    /// <summary>
    /// تاریخ ایجاد
    /// </summary>
    public String Create;
    /// <summary>
    /// تاریخ ویرایش
    /// </summary>
    public String Modified;
    /// <summary>
    /// فعال بود یا نبودن
    /// </summary>
    public boolean IsActive;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isTop() {
        return IsTop;
    }

    public void setTop(boolean top) {
        IsTop = top;
    }

    public int getRows() {
        return Rows;
    }

    public void setRows(int rows) {
        Rows = rows;
    }

    public int getCols() {
        return Cols;
    }

    public void setCols(int cols) {
        Cols = cols;
    }

    public int getPriority() {
        return Priority;
    }

    public void setPriority(int priority) {
        Priority = priority;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCreate() {
        return Create;
    }

    public void setCreate(String create) {
        Create = create;
    }

    public String getModified() {
        return Modified;
    }

    public void setModified(String modified) {
        Modified = modified;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }
}
