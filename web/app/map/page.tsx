'use client';
import dynamic from 'next/dynamic';

const DynamicPage = dynamic(() => import('./mapPageComponent'), {
    ssr: false
});

export default DynamicPage;