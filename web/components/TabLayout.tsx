import {ReactNode, useState} from "react";
import {Tab, TabList, TabPanel, Tabs} from "@mui/joy";

/**
 * Tab layout element
 */
export interface TabLayoutElement {
    /**
     * ID of the tab
     */
    id: string;
    /**
     * Label of the tab
     */
    label: string;
    /**
     * Content of the tab
     */
    content: ReactNode;
}

interface TabLayoutProps {
    /**
     * All elements of the tab
     */
    elements: TabLayoutElement[];
}

/**
 * Simple tab layout
 *
 * @constructor
 */
const TabLayout = ({elements}: TabLayoutProps) => {

    const [value, setValue] = useState<string>(elements[0].id);

    return (
        <Tabs value={value} defaultValue={elements[0].id} onChange={(e, value) => setValue(value as string)}>
            <TabList>
                {elements.map((tab) => (
                    <Tab value={tab.id} key={tab.id}>{tab.label}</Tab>
                ))}
            </TabList>
            {elements.map((tab) => (
                <TabPanel value={tab.id} key={tab.id}>
                    {tab.content}
                </TabPanel>
            ))}
        </Tabs>
    );
}

export default TabLayout;