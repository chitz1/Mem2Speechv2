package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech;

import android.content.Context;

import java.lang.ref.WeakReference;

class WeakReferenceContextWrapper {
    private final WeakReference<Context> contextReference;

    public WeakReferenceContextWrapper(WeakReference<Context> contextReference) {
        this.contextReference = contextReference;
    }

    public WeakReference<Context> getContextReference() {
        return contextReference;
    }
}
