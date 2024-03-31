'use client';
import {Typography} from "@mui/joy";
import {useGetAllSettingsQuery} from "@/generated/graphql";
import LoadingSpinner from "@/components/LoadingSpinner";
import {useMemo} from "react";
import TabLayout, {TabLayoutElement} from "@/components/TabLayout";
import SettingsTab from "@/components/settings/SettingsTab";
import {useTranslation} from "next-export-i18n";


const Page = () => {

    const {data, loading} = useGetAllSettingsQuery();
    const {t} = useTranslation();

    const dataObj = useMemo<object>(() => {
        let obj = {};
        if (data?.allSettings) {
            for (const el of data?.allSettings!) {
                // @ts-ignore
                if(obj[`${el?.tab}`] === undefined) {
                    // @ts-ignore
                    obj[`${el?.tab}`] = {};
                }
                // @ts-ignore
                obj[`${el?.tab}`][`${el?.key}`] = el;
            }
        }
        return obj;
    }, [data?.allSettings]);

    const tabs = useMemo<TabLayoutElement[]>(() => Object.keys(dataObj).map(
        (el) => ({
            id: el,
            label: t(`settings.tab.${el}`),
            // @ts-ignore
            content: <SettingsTab dataObj={dataObj[el]} />
        })
    ), [dataObj, t]);

    return (
        <LoadingSpinner loading={loading}>
            <TabLayout elements={tabs} />
        </LoadingSpinner>
    )
}

export const dynamic = 'force-static';
export const dynamicParams = true;

export default Page;