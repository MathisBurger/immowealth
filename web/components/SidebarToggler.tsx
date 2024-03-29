import React from "react";
import {Dispatch, ReactNode, SetStateAction, useState} from "react";
import {Box} from "@mui/joy";

interface SidebarTogglerProps {
    /**
     * if expanded by default
     */
    defaultExpanded?: boolean;
    /**
     * Children
     */
    children: ReactNode;
    /**
     * Renders the toggle
     * @param params
     */
    renderToggle: (params: {
        open: boolean;
        setOpen: Dispatch<SetStateAction<boolean>>;
    }) => ReactNode;
}

/**
 * The sidebar toggler
 *
 * @constructor
 */
const SidebarToggler = ({
 defaultExpanded = false,
 renderToggle,
 children,
}: SidebarTogglerProps) => {

    const [open, setOpen] = useState(defaultExpanded);

    return (
        <React.Fragment>
            {renderToggle({ open, setOpen })}
            <Box
                sx={{
                    display: 'grid',
                    gridTemplateRows: open ? '1fr' : '0fr',
                    transition: '0.2s ease',
                    '& > *': {
                        overflow: 'hidden',
                    },
                }}
            >
                {children}
            </Box>
        </React.Fragment>
    );
}
export default SidebarToggler;