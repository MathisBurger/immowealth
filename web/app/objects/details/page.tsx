'use client';

import dynamic from 'next/dynamic';

const DynamicPage = dynamic(() => import('./pageComponent'), {
    ssr: false
});

export default DynamicPage;