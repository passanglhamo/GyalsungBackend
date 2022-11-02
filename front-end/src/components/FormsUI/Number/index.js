import React from 'react';
import { TextField } from '@material-ui/core';
import { useField } from 'formik';

const NumberWrapper = ({
                              name,
                              ...otherProps
                          }) => {
    const [field, mata] = useField(name);

    const configTextfield = {
        ...field,
        ...otherProps,
        fullWidth: true,
        variant: 'standard',
        type:'number'
    };

    if (mata && mata.touched && mata.error) {
        configTextfield.error = true;
        configTextfield.helperText = mata.error;
    }

    return (
        <TextField {...configTextfield} />
    );
};

export default NumberWrapper;