import React from 'react';
import TextareaAutosize from '@mui/base/TextareaAutosize';
import {useField} from 'formik';

const TextAreafieldWrapper = ({
                                  name,
                                  placeholder,
                                  ...otherProps
                              }) => {
    const [field, mata] = useField(name);

    const configTextfield = {
        ...field,
        ...otherProps,
    };

    if (mata && mata.touched && mata.error) {
        configTextfield.error = true;
        configTextfield.helperText = mata.error;
    }

    return (
        <TextareaAutosize {...configTextfield} minRows={3} style={{width: 500}} placeholder={placeholder}
        />
    );
};

export default TextAreafieldWrapper;