package test.task.rest_user.domain;

public final class Views {
    public interface Id {}

    public interface IdName extends Id {}

    public interface UserAndRoles extends IdName {}

    public interface FullUser extends UserAndRoles {}
}
