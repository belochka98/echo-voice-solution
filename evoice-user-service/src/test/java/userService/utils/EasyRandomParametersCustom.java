package userService.utils;

import org.jeasy.random.FieldPredicates;
import userService.entity.User;

public class EasyRandomParametersCustom extends org.jeasy.random.EasyRandomParameters {
    public EasyRandomParametersCustom() {
        this.setUserPredicates();
    }

    private void setUserPredicates() {
        this.randomize(
                FieldPredicates.inClass(User.class).and(FieldPredicates.named("phone")),
                () -> "9999999999"
        );
    }
}
