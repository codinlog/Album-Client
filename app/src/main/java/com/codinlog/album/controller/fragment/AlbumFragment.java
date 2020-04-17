package com.codinlog.album.controller.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codinlog.album.R;
import com.codinlog.album.adapter.kotlin.AlbumFolderAdapter;
import com.codinlog.album.adapter.kotlin.AlbumAdapter;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.bean.kotlin.FolderBean;
import com.codinlog.album.controller.BaseFragmentController;
import com.codinlog.album.controller.activity.kotlin.AlbumPreviewActivity;
import com.codinlog.album.databinding.AlbumFragmentBinding;
import com.codinlog.album.model.AlbumViewModel;
import com.codinlog.album.util.DataStore;
import com.codinlog.album.util.WorthStore;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import kotlin.Triple;

public class AlbumFragment extends BaseFragmentController<AlbumViewModel, AlbumFragmentBinding> {
    private AlbumAdapter albumAdapter;
    private AlbumFolderAdapter albumFolderAdapter;
    private BottomSheetBehavior sheetBehavior;
    private RecyclerView rvBottom;
    private Handler handler = new Handler();

    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.album_fragment;
    }

    @Override
    public void doInitViewData() {
        viewModel = new ViewModelProvider(getActivity()).get(AlbumViewModel.class);
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        rvBottom = binding.getRoot().findViewById(R.id.rvBottom);
    }

    @Override
    public void doInitListener() {
        viewModel.getDisplayData().observe(getViewLifecycleOwner(), displayData -> {
            albumAdapter.setDisplayData(displayData);
            if (displayData.size() <= 0)
                binding.noticeLayout.setVisibility(View.VISIBLE);
            else
                binding.noticeLayout.setVisibility(View.INVISIBLE);
        });
        viewModel.getSelectedData().observe(getViewLifecycleOwner(), o -> {
            if (viewModel.getMode().getValue() == WorthStore.MODE.MODE_SELECT) {
                int displaySize = viewModel.getDisplayData().getValue().size();
                int selectSize = viewModel.getSelectedData().getValue().size();
                boolean allSelect = selectSize >= displaySize;
                viewModel.mainViewModel.setIsSelectAll(allSelect);
            }
            viewModel.mainViewModel.setTitle();
        });
        viewModel.getIsSelectAll().observe(getViewLifecycleOwner(), isAll -> {
            albumAdapter.notifyDataSetChanged();
        });
        viewModel.getMode().observe(getViewLifecycleOwner(), mode -> {
            if (albumAdapter != null)
                albumAdapter.setMode(mode);
            if (mode == WorthStore.MODE.MODE_NORMAL)
                viewModel.resetSelectData();
        });
        viewModel.getDisplayOption().observe(getViewLifecycleOwner(), option -> {
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN)
                sheetBehavior.setState(option);
        });
        viewModel.getFolderDisplayData().observe(getViewLifecycleOwner(), map -> {
            List list = new ArrayList(map.keySet());
            Collections.sort(list);
            albumFolderAdapter.setDisplayData(list);
        });
    }

    @Override
    public void doInitDisplayData() {
        albumAdapter = new AlbumAdapter(o -> {
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN && viewModel.getMode().getValue() == WorthStore.MODE.MODE_NORMAL) {
                Intent intent = new Intent(getContext(), AlbumPreviewActivity.class);
                intent.putExtra("from", "album");
                intent.putExtra("fromValue", viewModel.getDisplayData().getValue().get((int) o));
                startActivity(intent);
            } else {
                viewModel.setSelectedData((int) o);
                albumAdapter.notifyItemChanged((int) o, "payload");
            }
        }, o -> {
            if (viewModel.getMode().getValue() == WorthStore.MODE.MODE_NORMAL)
                viewModel.mainViewModel.setMode(WorthStore.MODE.MODE_SELECT);
            viewModel.setSelectedData((int) o);
            albumAdapter.notifyItemChanged((int) o, "payload");
        });
        albumFolderAdapter = new AlbumFolderAdapter(o -> {
            FolderBean folderBean = (FolderBean) o;
            DataStore.getInstance().setFolderDisplayData(viewModel.getFolderDisplayData().getValue().get(folderBean));
            Intent intent = new Intent(getContext(), AlbumPreviewActivity.class);
            intent.putExtra("from", "albumFolder");
            intent.putExtra("fromValue", folderBean.getFolderName());
            startActivity(intent);
        }, o -> {
            WeakReference<Triple<FolderBean, View, Integer>> reference = (WeakReference<Triple<FolderBean, View, Integer>>) o;
            Triple<FolderBean, View, Integer> folderBeanViewPair = reference.get();
            PopupMenu popupMenu = new PopupMenu(getContext(), folderBeanViewPair.component2());
            popupMenu.inflate(R.menu.delete_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.delete) {
                    FolderBean folderBean = folderBeanViewPair.component1();
                    List<PhotoBean> photoBeans = viewModel.getFolderDisplayData().getValue().get(folderBean);
                    boolean isDeleteAll = deletePhotoBeans(photoBeans, folderBean);
                    if (!isDeleteAll)
                        Toast.makeText(getContext(), R.string.delete_not_all, Toast.LENGTH_SHORT).show();
                    else
                        viewModel.getFolderDisplayData().getValue().remove(folderBean);
                    albumFolderAdapter.notifyItemRemoved(folderBeanViewPair.component3());
                    handler.postDelayed(() -> viewModel.setFolderDisplayData(viewModel.getFolderDisplayData().getValue()), 550);
                }
                return true;
            });
            popupMenu.show();
        });
        binding.rv.setLayoutManager(new GridLayoutManager(getContext(), WorthStore.albumItemNum));
        binding.rv.setAdapter(albumAdapter);
        rvBottom.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBottom.setAdapter(albumFolderAdapter);
        rvBottom.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    public boolean deletePhotoBeans(List<PhotoBean> photoBeans, FolderBean folderBean) {
        Iterator<PhotoBean> iterator = photoBeans.iterator();
        while (iterator.hasNext()) {
            PhotoBean photoBean = iterator.next();
            File file = new File(photoBean.getPhotoPath());
            if (file.exists() && file.isFile() && file.delete())
                iterator.remove();
        }
        getContext().getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Media.DATA + "=\"" + folderBean.getFolderPath() + "\"", null);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(new File(Environment.getExternalStorageState()));
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
        return photoBeans.size() <= 0;
    }
}
