import {ReactNode, useState} from "react";
import {Tab, TabList, TabPanel, Tabs} from "@mui/joy";

export interface TabLayoutElement {
    id: string;
    label: string;
    content: ReactNode;
}

interface TabLayoutProps {
    elements: TabLayoutElement[];
}

const TabLayout = ({elements}: TabLayoutProps) => {

    const [value, setValue] = useState<string>(elements[0].id);

    return (
        <Tabs value={value} defaultValue={elements[0].id} onChange={(e, value) => setValue(value as string)}>
            <TabList>
                {elements.map((tab) => (
                    <Tab value={tab.id}>{tab.label}</Tab>
                ))}
            </TabList>
            {elements.map((tab) => (
                <TabPanel value={tab.id}>
                    {tab.content}
                </TabPanel>
            ))}
        </Tabs>
    );
}

export default TabLayout;