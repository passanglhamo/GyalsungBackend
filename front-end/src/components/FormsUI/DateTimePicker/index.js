import {React,useState} from 'react';
import DateFnsUtils from "@date-io/date-fns";
import { useField } from 'formik';
import {KeyboardDatePicker, MuiPickersUtilsProvider} from "@material-ui/pickers";

const DateTimePicker = ({
                            name,
                            ...otherProps
                        }) => {
    const [field, meta] = useField(name);
    const configDateTimePicker = {
        ...field,
        ...otherProps,
        margin: "normal",
        id: "date-picker-dialog",
        format: "dd/MM/yyyy"
    };


    if(meta && meta.touched && meta.error) {
        configDateTimePicker.error = true;
        configDateTimePicker.helperText = meta.error;
    }

    return (
        <MuiPickersUtilsProvider utils={DateFnsUtils}>
            <KeyboardDatePicker
                {...configDateTimePicker}
            />
        </MuiPickersUtilsProvider>
    );
};

export default DateTimePicker;